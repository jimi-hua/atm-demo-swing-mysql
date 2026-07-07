package yefan;

import yefan.dao.UserDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Login extends JFrame {

    private boolean loginSuccess = false;

    public Login() {
        // 主面板 — BorderLayout：上方图片，中间登录表单
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- 顶部：Logo 图片 ----
        URL imgUrl = getClass().getClassLoader().getResource("atm_logo.jpg");
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            // 缩放图片：宽度适应窗口，保持比例
            Image scaledImg = icon.getImage().getScaledInstance(380, 180, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(logoLabel, BorderLayout.NORTH);
        }

        // ---- 中间：登录表单 ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JTextField usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("密   码：");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        JButton cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        btnPanel.add(loginButton);
        btnPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(btnPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        this.add(mainPanel);
        this.setTitle("ATM XXBank - 登录");
        this.setSize(450, 480);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                UserDao userDao = Atm.getUserDao();
                User matchedUser = userDao.findByNameAndPassword(username, password);

                if (matchedUser != null) {
                    if (!matchedUser.isEnable()) {
                        JOptionPane.showMessageDialog(Login.this, "账户已冻结，无法登录！", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    loginSuccess = true;
                    Atm.currentUser = matchedUser;
                    dispose();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    new AtmFrame();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "用户名或密码错误！", "提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginSuccess = false;
                dispose();
            }
        });

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}
