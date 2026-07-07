package yefan;

import java.sql.Timestamp;

/** 交易记录实体 */
public class Transaction {
    private int id;
    private int userId;
    private String type;        // deposit/withdraw/transfer_out/transfer_in
    private double amount;
    private String relatedAccount;
    private double balanceAfter;
    private Timestamp createdAt;

    public Transaction() {}

    public Transaction(int id, int userId, String type, double amount,
                       String relatedAccount, double balanceAfter, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.relatedAccount = relatedAccount;
        this.balanceAfter = balanceAfter;
        this.createdAt = createdAt;
    }

    // 用于插入的构造方法（无id和时间）
    public Transaction(int userId, String type, double amount,
                       String relatedAccount, double balanceAfter) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.relatedAccount = relatedAccount;
        this.balanceAfter = balanceAfter;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getRelatedAccount() { return relatedAccount; }
    public void setRelatedAccount(String relatedAccount) { this.relatedAccount = relatedAccount; }
    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /** 交易类型中文名 */
    public String getTypeName() {
        switch (type) {
            case "deposit":      return "存款";
            case "withdraw":     return "取款";
            case "transfer_out": return "转出";
            case "transfer_in":  return "转入";
            default:             return type;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %.2f元  余额:%.2f  %s",
                getTypeName(), amount >= 0 ? "+" : "", amount, balanceAfter,
                createdAt != null ? createdAt.toString().substring(0, 19) : "");
    }
}
