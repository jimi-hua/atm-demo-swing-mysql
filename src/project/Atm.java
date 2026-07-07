package project;

import project.dao.UserDao;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class Atm {

    public static User currentUser = null;
    private static final UserDao userDao = new UserDao();
    public static Scanner sc = new Scanner(System.in);

    public Atm() {
        System.out.println("请选择业务");
        System.out.println("1:开户  2:登录  3:查询账号余额  4:查询用户列表  5:存钱  6:取钱  7:转账  0:退出");
        loop:
        while (true) {
            int i = sc.nextInt();
            sc.nextLine();
            switch (i) {
                case 1:
                    register(sc);
                    break;
                case 2:
                    login(sc);
                    break;
                case 3:
                    queryBalance();
                    break;
                case 4:
                    showAllAccountList();
                    break;
                case 5:
                    cunqian(sc);
                    break;
                case 6:
                    quqian(new JTextField());
                    break;
                case 7:
                    zhuanzhang(sc);
                    break;
                case 0:
                    System.out.println("退出");
                    break loop;
                default:
                    System.out.println("请重新输入");
                    break;
            }
        }
    }

    private static void register(Scanner sc) {
        System.out.println("请输入用户名");
        String name = sc.nextLine();
        if (name.trim().length() <= 0) {
            System.out.println("用户名不能为空");
            return;
        }
        System.out.println("请输入身份证号");
        String idCard = sc.nextLine();
        System.out.println("请输入卡号");
        String account = sc.nextLine();

        User existing = userDao.findByAccount(account);
        if (existing != null) {
            System.out.println("该卡号已被注册，请更换卡号！");
            return;
        }

        System.out.println("请输入密码");
        String password = sc.nextLine();
        System.out.println("请输入初始余额");
        double balance = sc.nextDouble();
        sc.nextLine();

        User user = new User(name, idCard, balance, account, password, true);
        int generatedId = userDao.insert(user);
        if (generatedId > 0) {
            System.out.println("开户成功！");
        } else {
            System.out.println("开户失败，请重试！");
        }
    }

    private static void login(Scanner sc) {
        System.out.println("登录");
        System.out.println("请输入卡号：");
        String account = sc.nextLine();
        System.out.println("请输入密码：");
        String loginPassword = sc.nextLine();

        User u = userDao.findByAccount(account);
        if (u != null && u.getPassword().equals(loginPassword)) {
            if (!u.isEnable()) {
                System.out.println("账户已冻结，无法登录！");
                return;
            }
            currentUser = u;
            System.out.println("登录成功！欢迎您，" + u.getName());
        } else {
            System.out.println("登录失败！卡号或密码错误");
        }
    }

    public static void queryBalance() {
        System.out.println("\n===== 查询余额 =====");
        if (currentUser == null) {
            System.out.println("请先登录！");
            return;
        }
        User fresh = userDao.findByAccount(currentUser.getAccount());
        if (fresh != null) {
            currentUser.setBalance(fresh.getBalance());
        }
        System.out.println("当前登录用户：" + currentUser.getName());
        System.out.println("卡号：" + currentUser.getAccount());
        System.out.println("当前余额：" + currentUser.getBalance() + " 元");
    }

    private static void showAllAccountList() {
        if (currentUser == null) {
            System.out.println("请先登录！");
            return;
        }
        System.out.println("========全部用户账号列表========");
        List<User> users = userDao.findAll();
        for (User u : users) {
            System.out.println(u);
        }
    }

    private static void cunqian(Scanner sc) {
        if (currentUser == null) {
            System.out.println("请先登录！");
            return;
        }
        System.out.println("=====存钱业务=====");
        System.out.print("请输入您要存入的金额：");
        double deposit = sc.nextDouble();
        sc.nextLine();
        if (deposit > 0) {
            double newBalance = currentUser.getBalance() + deposit;
            boolean ok = userDao.updateBalance(currentUser.getId(), newBalance);
            if (ok) {
                currentUser.setBalance(newBalance);
                System.out.println("存钱成功，当前余额：" + currentUser.getBalance() + "元");
            } else {
                System.out.println("存钱失败！");
            }
        } else {
            System.out.println("存款金额必须大于0");
        }
    }

    static void quqian(JTextField x) {
        System.out.println("取钱");
        if (currentUser == null) {
            System.out.println("请先登录账号才能取钱！");
            return;
        }
        double takeMoney = Double.parseDouble(x.getText());
        if (takeMoney <= 0) {
            System.out.println("取款金额必须大于0！");
        } else if (takeMoney > currentUser.getBalance()) {
            System.out.println("余额不足，当前余额：" + currentUser.getBalance());
        } else {
            double newBalance = currentUser.getBalance() - takeMoney;
            boolean ok = userDao.updateBalance(currentUser.getId(), newBalance);
            if (ok) {
                currentUser.setBalance(newBalance);
                System.out.println("取款成功！取出" + takeMoney + "元，剩余余额：" + currentUser.getBalance());
            } else {
                System.out.println("取款失败！");
            }
        }
    }

    static void zhuanzhang(Scanner sc) {
        if (currentUser == null) {
            System.out.println("请先登录！");
            return;
        }
        System.out.println("=====转账业务=====");
        System.out.print("请输入收款卡号：");
        String targetAccount = sc.nextLine();
        System.out.print("请输入转账金额：");
        double transferMoney = sc.nextDouble();
        sc.nextLine();

        if (transferMoney <= 0) {
            System.out.println("转账金额必须大于0！");
            return;
        }
        if (currentUser.getBalance() < transferMoney) {
            System.out.println("余额不足，转账失败！");
            return;
        }

        User targetUser = userDao.findByAccount(targetAccount);
        if (targetUser == null) {
            System.out.println("收款卡号不存在，转账失败！");
            return;
        }
        if (targetUser.getAccount().equals(currentUser.getAccount())) {
            System.out.println("不能向自己账户转账！");
            return;
        }
        if (!targetUser.isEnable()) {
            System.out.println("对方账户已冻结，无法转账！");
            return;
        }
        if (!currentUser.isEnable()) {
            System.out.println("您的账户已冻结，无法转账！");
            return;
        }

        double myNewBalance = currentUser.getBalance() - transferMoney;
        double targetNewBalance = targetUser.getBalance() + transferMoney;

        boolean myOk = userDao.updateBalance(currentUser.getId(), myNewBalance);
        boolean targetOk = userDao.updateBalance(targetUser.getId(), targetNewBalance);

        if (myOk && targetOk) {
            currentUser.setBalance(myNewBalance);
            targetUser.setBalance(targetNewBalance);
            System.out.println("转账成功！");
            System.out.println("您转出金额：" + transferMoney + "元");
            System.out.println("当前余额：" + currentUser.getBalance() + "元");
            System.out.println("收款用户：" + targetUser.getName());
        } else {
            System.out.println("转账失败，请重试！");
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static UserDao getUserDao() {
        return userDao;
    }
}
