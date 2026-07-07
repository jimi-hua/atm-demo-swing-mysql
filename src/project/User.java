package project;

public class User {
    private int id;
    private String name;
    private String idCard;
    private double balance;
    private String account;
    private String password;
    private boolean enable;

    public User(String name, String idCard, double balance, String account, String password, boolean enable) {
        this.id = 0;
        this.name = name;
        this.idCard = idCard;
        this.balance = balance;
        this.account = account;
        this.password = password;
        this.enable = enable;
    }

    public User(int id, String name, String idCard, double balance, String account, String password, boolean enable) {
        this.id = id;
        this.name = name;
        this.idCard = idCard;
        this.balance = balance;
        this.account = account;
        this.password = password;
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", balance=" + balance +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                '}';
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isEnable() { return enable; }
    public void setEnable(boolean enable) { this.enable = enable; }
}
