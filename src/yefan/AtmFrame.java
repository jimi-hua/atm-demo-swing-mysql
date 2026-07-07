package yefan;

import yefan.Transaction;
import yefan.dao.UserDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AtmFrame extends JFrame {

    private static final UserDao userDao = Atm.getUserDao();

    public AtmFrame() {
        User currentUser = Atm.getCurrentUser();

        this.setTitle("ATM");
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.PINK);

        this.initComponent();
        this.setVisible(true);
    }

    public void initComponent() {
        User currentUser = Atm.getCurrentUser();
        JPanel contentPane = (JPanel) this.getContentPane();

        // 左边面板
        JPanel westPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        westPanel.setBorder(new EmptyBorder(30, 20, 10, 20));
        JButton jButton1 = getButton("查询");
        JButton jButton2 = getButton("存款");
        JButton jButton3 = getButton("取款");
        JButton jButton4 = getButton("挂失");
        JButton jButton5 = getButton("找回密码");
        westPanel.add(jButton1);
        westPanel.add(jButton2);
        westPanel.add(jButton3);
        westPanel.add(jButton4);
        westPanel.add(jButton5);
        westPanel.setOpaque(false);

        // 中间面板
        JPanel centerPanel = new JPanel();
        JLabel l1 = new JLabel(currentUser.getName() + " 欢迎您");
        JLabel l2 = new JLabel("XXBank");
        JLabel l3 = new JLabel("请选择服务");
        centerPanel.add(l1);
        centerPanel.add(l2);
        centerPanel.add(l3);
        l1.setFont(new Font("微软雅黑", Font.BOLD, 30));
        l2.setFont(new Font("微软雅黑", Font.BOLD, 30));
        l3.setFont(new Font("微软雅黑", Font.BOLD, 30));
        l1.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        l2.setBorder(new EmptyBorder(50, 0, 0, 0));
        l3.setBorder(new EmptyBorder(90, 0, 0, 0));
        centerPanel.setOpaque(false);

        // 右边面板
        JPanel eastPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        eastPanel.setBorder(new EmptyBorder(30, 20, 10, 10));
        JButton eastButton1 = getButton("转账");
        JButton eastButton2 = getButton("改密");
        JButton eastButton3 = getButton("开户");
        JButton eastButton4 = getButton("推卡");
        JButton eastButton5 = getButton("明细");
        eastPanel.add(eastButton1);
        eastPanel.add(eastButton2);
        eastPanel.add(eastButton3);
        eastPanel.add(eastButton4);
        eastPanel.add(eastButton5);
        eastPanel.setOpaque(false);

        contentPane.add(westPanel, BorderLayout.WEST);
        contentPane.add(centerPanel);
        contentPane.add(eastPanel, BorderLayout.EAST);

        // ---- 查询 ----
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = Atm.getCurrentUser();
                User fresh = userDao.findByAccount(user.getAccount());
                if (fresh != null) {
                    user.setBalance(fresh.getBalance());
                }
                JOptionPane.showMessageDialog(AtmFrame.this,
                        "卡号：" + user.getAccount()
                                + "\n余额：" + user.getBalance() + " 元"
                                + "\n状态：" + (user.isEnable() ? "正常" : "冻结"),
                        "查询余额", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // ---- 存款 ----
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = Atm.getCurrentUser();
                JPanel panel = new JPanel();
                panel.add(new JLabel("请输入存款金额: "));
                JTextField field = new JTextField(15);
                panel.add(field);
                int option = JOptionPane.showConfirmDialog(AtmFrame.this, panel,
                        "存款", JOptionPane.OK_CANCEL_OPTION);
                if (option != JOptionPane.OK_OPTION) return;

                try {
                    double amount = Double.parseDouble(field.getText().trim());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(AtmFrame.this, "金额必须大于0！");
                        return;
                    }
                    double newBalance = user.getBalance() + amount;
                    Transaction t = new Transaction(user.getId(), "deposit", amount, null, newBalance);
                    if (userDao.updateBalanceAndRecord(user.getId(), newBalance, t)) {
                        user.setBalance(newBalance);
                        JOptionPane.showMessageDialog(AtmFrame.this, "存款成功！余额：" + user.getBalance());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "请输入有效金额！");
                }
            }
        });

        // ---- 取款 ----
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = Atm.getCurrentUser();
                JPanel panel = new JPanel();
                panel.add(new JLabel("请输入取款金额: "));
                JTextField field = new JTextField(15);
                panel.add(field);
                int option = JOptionPane.showConfirmDialog(AtmFrame.this, panel,
                        "取款", JOptionPane.OK_CANCEL_OPTION);
                if (option != JOptionPane.OK_OPTION) return;

                try {
                    double amount = Double.parseDouble(field.getText().trim());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(AtmFrame.this, "金额必须大于0！");
                        return;
                    }
                    if (amount > user.getBalance()) {
                        JOptionPane.showMessageDialog(AtmFrame.this, "余额不足！当前余额：" + user.getBalance());
                        return;
                    }
                    double newBalance = user.getBalance() - amount;
                    Transaction t = new Transaction(user.getId(), "withdraw", amount, null, newBalance);
                    if (userDao.updateBalanceAndRecord(user.getId(), newBalance, t)) {
                        user.setBalance(newBalance);
                        JOptionPane.showMessageDialog(AtmFrame.this, "取款成功！余额：" + user.getBalance());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "请输入有效金额！");
                }
            }
        });

        // ---- 挂失 ----
        jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = Atm.getCurrentUser();
                if (!user.isEnable()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "账户已是冻结状态！");
                    return;
                }
                int cfm = JOptionPane.showConfirmDialog(AtmFrame.this,
                        "确认挂失账户？\n挂失后账户将被冻结，无法进行交易。\n\n卡号：" + user.getAccount()
                                + "\n姓名：" + user.getName(),
                        "挂失确认", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (cfm != JOptionPane.YES_OPTION) return;

                // 双重验证：输入身份证号
                String inputIdCard = JOptionPane.showInputDialog(AtmFrame.this,
                        "请输入身份证号以确认挂失：", "身份验证", JOptionPane.QUESTION_MESSAGE);
                if (inputIdCard == null || !inputIdCard.trim().equals(user.getIdCard())) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "身份证号不匹配，挂失取消！");
                    return;
                }

                if (userDao.updateEnable(user.getId(), false)) {
                    user.setEnable(false);
                    JOptionPane.showMessageDialog(AtmFrame.this, "挂失成功！账户已冻结。");
                } else {
                    JOptionPane.showMessageDialog(AtmFrame.this, "挂失失败，请重试！");
                }
            }
        });

        // ---- 找回密码 ----
        jButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(new EmptyBorder(10, 10, 10, 10));

                JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row1.add(new JLabel("姓  名："));
                JTextField nameField = new JTextField(15);
                row1.add(nameField);
                panel.add(row1);
                panel.add(Box.createVerticalStrut(8));

                JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row2.add(new JLabel("身份证号："));
                JTextField idCardField = new JTextField(15);
                row2.add(idCardField);
                panel.add(row2);

                int option = JOptionPane.showConfirmDialog(AtmFrame.this, panel,
                        "找回密码", JOptionPane.OK_CANCEL_OPTION);
                if (option != JOptionPane.OK_OPTION) return;

                String name = nameField.getText().trim();
                String idCard = idCardField.getText().trim();

                if (name.isEmpty() || idCard.isEmpty()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "姓名和身份证号不能为空！");
                    return;
                }

                User found = userDao.findByNameAndIdCard(name, idCard);
                if (found != null) {
                    JOptionPane.showMessageDialog(AtmFrame.this,
                            "身份验证成功！\n\n卡号：" + found.getAccount()
                                    + "\n密码：" + found.getPassword()
                                    + "\n\n请妥善保管您的密码！",
                            "密码找回", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AtmFrame.this, "姓名或身份证号不匹配！");
                }
            }
        });

        // ---- 改密 ----
        eastButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = Atm.getCurrentUser();

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(new EmptyBorder(10, 10, 10, 10));

                JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row1.add(new JLabel("原密码："));
                JPasswordField oldPwdField = new JPasswordField(15);
                row1.add(oldPwdField);
                panel.add(row1);
                panel.add(Box.createVerticalStrut(8));

                JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row2.add(new JLabel("新密码："));
                JPasswordField newPwdField = new JPasswordField(15);
                row2.add(newPwdField);
                panel.add(row2);
                panel.add(Box.createVerticalStrut(8));

                JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row3.add(new JLabel("确认密码："));
                JPasswordField confirmPwdField = new JPasswordField(15);
                row3.add(confirmPwdField);
                panel.add(row3);

                int option = JOptionPane.showConfirmDialog(AtmFrame.this, panel,
                        "修改密码", JOptionPane.OK_CANCEL_OPTION);
                if (option != JOptionPane.OK_OPTION) return;

                String oldPwd = new String(oldPwdField.getPassword());
                String newPwd = new String(newPwdField.getPassword());
                String confirmPwd = new String(confirmPwdField.getPassword());

                if (!oldPwd.equals(user.getPassword())) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "原密码错误！");
                    return;
                }
                if (newPwd.isEmpty()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "新密码不能为空！");
                    return;
                }
                if (!newPwd.equals(confirmPwd)) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "两次输入的新密码不一致！");
                    return;
                }
                if (oldPwd.equals(newPwd)) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "新密码不能与原密码相同！");
                    return;
                }

                if (userDao.updatePassword(user.getId(), newPwd)) {
                    user.setPassword(newPwd);
                    JOptionPane.showMessageDialog(AtmFrame.this, "密码修改成功！");
                } else {
                    JOptionPane.showMessageDialog(AtmFrame.this, "密码修改失败，请重试！");
                }
            }
        });

        // ---- 开户 ----
        eastButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(new EmptyBorder(10, 10, 10, 10));

                JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row1.add(new JLabel("姓  名："));
                JTextField nameField = new JTextField(15);
                row1.add(nameField);
                panel.add(row1);
                panel.add(Box.createVerticalStrut(5));

                JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row2.add(new JLabel("身份证号："));
                JTextField idCardField = new JTextField(15);
                row2.add(idCardField);
                panel.add(row2);
                panel.add(Box.createVerticalStrut(5));

                JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row3.add(new JLabel("卡  号："));
                JTextField accountField = new JTextField(15);
                row3.add(accountField);
                panel.add(row3);
                panel.add(Box.createVerticalStrut(5));

                JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row4.add(new JLabel("密  码："));
                JPasswordField pwdField = new JPasswordField(15);
                row4.add(pwdField);
                panel.add(row4);
                panel.add(Box.createVerticalStrut(5));

                JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row5.add(new JLabel("初始余额："));
                JTextField balanceField = new JTextField(15);
                row5.add(balanceField);
                panel.add(row5);

                int option = JOptionPane.showConfirmDialog(AtmFrame.this, panel,
                        "开户", JOptionPane.OK_CANCEL_OPTION);
                if (option != JOptionPane.OK_OPTION) return;

                String name = nameField.getText().trim();
                String idCard = idCardField.getText().trim();
                String account = accountField.getText().trim();
                String password = new String(pwdField.getPassword());
                String balanceText = balanceField.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "姓名不能为空！");
                    return;
                }
                if (idCard.isEmpty()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "身份证号不能为空！");
                    return;
                }
                if (account.isEmpty()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "卡号不能为空！");
                    return;
                }
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "密码不能为空！");
                    return;
                }

                double balance;
                try {
                    balance = Double.parseDouble(balanceText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "请输入有效的初始余额！");
                    return;
                }
                if (balance < 0) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "初始余额不能为负数！");
                    return;
                }

                // 检查卡号是否已存在
                User existing = userDao.findByAccount(account);
                if (existing != null) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "该卡号已被注册，请更换卡号！");
                    return;
                }

                User newUser = new User(name, idCard, balance, account, password, true);
                int generatedId = userDao.insert(newUser);
                if (generatedId > 0) {
                    JOptionPane.showMessageDialog(AtmFrame.this,
                            "开户成功！\n\n姓名：" + name
                                    + "\n卡号：" + account
                                    + "\n余额：" + balance + " 元");
                } else {
                    JOptionPane.showMessageDialog(AtmFrame.this, "开户失败，请重试！");
                }
            }
        });

        // ---- 推卡 ----
        eastButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cfm = JOptionPane.showConfirmDialog(AtmFrame.this,
                        "确认退卡？\n\n退卡后将返回登录界面。",
                        "退卡确认", JOptionPane.YES_NO_OPTION);
                if (cfm != JOptionPane.YES_OPTION) return;

                Atm.currentUser = null;
                dispose();
                // 返回登录界面
                new Login();
            }
        });

        // ---- 明细 ----
        eastButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = Atm.getCurrentUser();
                java.util.List<Transaction> list = userDao.findTransactionsByUserId(user.getId());

                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(AtmFrame.this,
                            "暂无交易记录！",
                            "交易明细", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("===== 交易明细（最近50条）=====\n\n");
                sb.append(String.format("%-6s %-10s %-8s %-10s %s\n",
                        "类型", "金额", "余额", "对方账号", "时间"));
                sb.append("--------------------------------------------------------------\n");
                for (Transaction t : list) {
                    String related = t.getRelatedAccount() != null ? t.getRelatedAccount() : "-";
                    String time = t.getCreatedAt() != null ? t.getCreatedAt().toString().substring(0, 19) : "-";
                    sb.append(String.format("%-6s %-10.2f %-8.2f %-10s %s\n",
                            t.getTypeName(), t.getAmount(), t.getBalanceAfter(), related, time));
                }

                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(550, 400));

                JOptionPane.showMessageDialog(AtmFrame.this, scrollPane,
                        "交易明细 - " + user.getName(), JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // ---- 转账 ----
        eastButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User cur = Atm.getCurrentUser();

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(new EmptyBorder(10, 10, 10, 10));

                JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row1.add(new JLabel("收款卡号："));
                JTextField accountField = new JTextField(15);
                row1.add(accountField);
                panel.add(row1);
                panel.add(Box.createVerticalStrut(8));

                JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row2.add(new JLabel("转账金额："));
                JTextField amountField = new JTextField(15);
                row2.add(amountField);
                panel.add(row2);

                int option = JOptionPane.showConfirmDialog(AtmFrame.this, panel,
                        "转账", JOptionPane.OK_CANCEL_OPTION);
                if (option != JOptionPane.OK_OPTION) return;

                String targetAccount = accountField.getText().trim();
                String amountText = amountField.getText().trim();

                if (targetAccount.isEmpty()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "请输入收款卡号！");
                    return;
                }

                double money;
                try {
                    money = Double.parseDouble(amountText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "请输入有效金额！");
                    return;
                }

                if (money <= 0) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "金额必须大于0！");
                    return;
                }
                if (money > cur.getBalance()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "余额不足！");
                    return;
                }
                if (targetAccount.equals(cur.getAccount())) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "不能向自己转账！");
                    return;
                }

                User target = userDao.findByAccount(targetAccount);
                if (target == null) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "收款卡号不存在！");
                    return;
                }
                if (!target.isEnable()) {
                    JOptionPane.showMessageDialog(AtmFrame.this, "对方账户已冻结！");
                    return;
                }

                // 二次确认
                int cfm = JOptionPane.showConfirmDialog(AtmFrame.this,
                        "确认转账？\n\n收款人：" + target.getName()
                                + "\n卡号：" + targetAccount
                                + "\n金额：" + money + " 元",
                        "确认", JOptionPane.YES_NO_OPTION);
                if (cfm != JOptionPane.YES_OPTION) return;

                double myNew = cur.getBalance() - money;
                double tarNew = target.getBalance() + money;

                if (userDao.updateBalance(cur.getId(), myNew) && userDao.updateBalance(target.getId(), tarNew)) {
                    // 记录双方的交易流水
                    userDao.insertTransaction(new Transaction(cur.getId(), "transfer_out", money, targetAccount, myNew));
                    userDao.insertTransaction(new Transaction(target.getId(), "transfer_in", money, cur.getAccount(), tarNew));

                    cur.setBalance(myNew);
                    target.setBalance(tarNew);
                    JOptionPane.showMessageDialog(AtmFrame.this,
                            "转账成功！\n收款人：" + target.getName()
                                    + "\n金额：" + money + " 元\n余额：" + cur.getBalance() + " 元");
                } else {
                    JOptionPane.showMessageDialog(AtmFrame.this, "转账失败，数据库异常！");
                }
            }
        });
    }

    private JButton getButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        button.setPreferredSize(new Dimension(120, 60));
        return button;
    }
}
