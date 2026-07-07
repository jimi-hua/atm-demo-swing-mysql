package yefan.dao;

import yefan.Transaction;
import yefan.User;
import yefan.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public User findByAccount(String account) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, id_card, balance, account, password, enable FROM users WHERE account = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, account);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rowToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] findByAccount 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, rs);
        }
        return null;
    }

    public User findByNameAndPassword(String name, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, id_card, balance, account, password, enable FROM users WHERE name = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rowToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] findByNameAndPassword 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, rs);
        }
        return null;
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, name, id_card, balance, account, password, enable FROM users");
            while (rs.next()) {
                list.add(rowToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] findAll 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        return list;
    }

    public int insert(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO users (name, id_card, balance, account, password, enable) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getIdCard());
            ps.setDouble(3, user.getBalance());
            ps.setString(4, user.getAccount());
            ps.setString(5, user.getPassword());
            ps.setBoolean(6, user.isEnable());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    user.setId(generatedId);
                    return generatedId;
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] insert 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, rs);
        }
        return -1;
    }

    public boolean updateBalance(int id, double newBalance) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET balance = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, newBalance);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDao] updateBalance 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, null);
        }
        return false;
    }

    private User rowToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String idCard = rs.getString("id_card");
        double balance = rs.getDouble("balance");
        String account = rs.getString("account");
        String password = rs.getString("password");
        boolean enable = rs.getBoolean("enable");
        return new User(id, name, idCard, balance, account, password, enable);
    }

    // ==================== 新增方法 ====================

    /** 通过姓名和身份证号查找用户（找回密码用） */
    public User findByNameAndIdCard(String name, String idCard) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, id_card, balance, account, password, enable FROM users WHERE name = ? AND id_card = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, idCard);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rowToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] findByNameAndIdCard 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, rs);
        }
        return null;
    }

    /** 修改密码 */
    public boolean updatePassword(int id, String newPassword) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET password = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDao] updatePassword 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, null);
        }
        return false;
    }

    /** 冻结/解冻账户 */
    public boolean updateEnable(int id, boolean enable) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET enable = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, enable);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDao] updateEnable 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, null);
        }
        return false;
    }

    /** 插入交易记录 */
    public boolean insertTransaction(Transaction t) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO transactions (user_id, type, amount, related_account, balance_after) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, t.getUserId());
            ps.setString(2, t.getType());
            ps.setDouble(3, t.getAmount());
            ps.setString(4, t.getRelatedAccount());
            ps.setDouble(5, t.getBalanceAfter());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDao] insertTransaction 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, null);
        }
        return false;
    }

    /** 查询用户的交易记录（最近50条） */
    public List<Transaction> findTransactionsByUserId(int userId) {
        List<Transaction> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, user_id, type, amount, related_account, balance_after, created_at FROM transactions WHERE user_id = ? ORDER BY created_at DESC LIMIT 50";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("related_account"),
                        rs.getDouble("balance_after"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] findTransactionsByUserId 失败：" + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, ps, rs);
        }
        return list;
    }

    /** 更新余额 + 记录交易（事务） */
    public boolean updateBalanceAndRecord(int id, double newBalance, Transaction t) {
        Connection conn = null;
        PreparedStatement psBalance = null;
        PreparedStatement psTrans = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. 更新余额
            String sql1 = "UPDATE users SET balance = ? WHERE id = ?";
            psBalance = conn.prepareStatement(sql1);
            psBalance.setDouble(1, newBalance);
            psBalance.setInt(2, id);
            int r1 = psBalance.executeUpdate();

            // 2. 插入交易记录
            String sql2 = "INSERT INTO transactions (user_id, type, amount, related_account, balance_after) VALUES (?, ?, ?, ?, ?)";
            psTrans = conn.prepareStatement(sql2);
            psTrans.setInt(1, t.getUserId());
            psTrans.setString(2, t.getType());
            psTrans.setDouble(3, t.getAmount());
            psTrans.setString(4, t.getRelatedAccount());
            psTrans.setDouble(5, t.getBalanceAfter());
            int r2 = psTrans.executeUpdate();

            if (r1 > 0 && r2 > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[UserDao] updateBalanceAndRecord 失败：" + e.getMessage());
            return false;
        } finally {
            try { if (psBalance != null) psBalance.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psTrans != null) psTrans.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
