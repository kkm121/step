package com.gdb.repository;

import com.gdb.db.ConnectionProvider;
import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.CurrentAccount;
import com.gdb.domain.IAccount;
import com.gdb.domain.SavingsAccount;
import com.gdb.exceptions.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountRepository implements AccountRepository {

    private final ConnectionProvider provider;

    public JdbcAccountRepository(ConnectionProvider provider) {
        this.provider=provider;
    }

    private int extractId(IAccount account) {
        if (account==null) {
            return 0;
        }
        try {
            return Integer.parseInt(account.getAccountNumber().replaceAll("\\D", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void save(IAccount account) {
        if (account==null) {
            return;
        }
        String sql="INSERT INTO accounts (account_number, account_holder_name, age, balance, account_type, status, pin, opening_date, overdraft_used, interest_earned) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn=provider.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)) {
            int accNum=extractId(account);
            int pin=0;
            if (account instanceof Account acc&&acc.getPin()!=null) {
                try {
                    pin=Integer.parseInt(acc.getPin());
                } catch (Exception ignored) {
                }
            }
            double overdraft=0.0;
            if (account instanceof CurrentAccount ca) {
                overdraft=ca.getOverdraftLimit();
            }
            double interest=0.0;
            if (account instanceof SavingsAccount sa) {
                interest=sa.getInterestRate();
            }
            int age=18;
            if (account instanceof Account acc) {
                age=acc.getAge();
            }
            ps.setInt(1,accNum);
            ps.setString(2,account.getName());
            ps.setInt(3,Math.max(18,age));
            ps.setDouble(4,account.getBalance());
            ps.setString(5,account.getAccountType());
            ps.setString(6,account.getStatus()!=null?account.getStatus():"Active");
            ps.setInt(7,pin);
            ps.setString(8,LocalDate.now().toString());
            ps.setDouble(9,overdraft);
            ps.setDouble(10,interest);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save account: " + e.getMessage(),e);
        }
    }

    @Override
    public IAccount findById(int accountNumber) {
        String sql="SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn=provider.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1,accountNumber);
            try (ResultSet rs=ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find account by ID: " + e.getMessage(),e);
        }
    }

    @Override
    public List<IAccount> findAll() {
        List<IAccount> accounts=new ArrayList<>();
        String sql="SELECT * FROM accounts";
        try (Connection conn=provider.getConnection();
             Statement stmt=conn.createStatement();
             ResultSet rs=stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(mapRow(rs));
            }
            return accounts;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all accounts: " + e.getMessage(),e);
        }
    }

    @Override
    public void update(IAccount account) {
        if (account==null) {
            return;
        }
        String sql="UPDATE accounts SET balance = ?, status = ?, overdraft_used = ?, interest_earned = ? WHERE account_number = ?";
        try (Connection conn=provider.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)) {
            int accNum=extractId(account);
            double overdraft=0.0;
            if (account instanceof CurrentAccount ca) {
                overdraft=ca.getOverdraftLimit();
            }
            double interest=0.0;
            if (account instanceof SavingsAccount sa) {
                interest=sa.getInterestRate();
            }
            ps.setDouble(1,account.getBalance());
            ps.setString(2,account.getStatus()!=null?account.getStatus():"Active");
            ps.setDouble(3,overdraft);
            ps.setDouble(4,interest);
            ps.setInt(5,accNum);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update account: " + e.getMessage(),e);
        }
    }

    @Override
    public void delete(int accountNumber) {
        String sql="DELETE FROM accounts WHERE account_number = ?";
        try (Connection conn=provider.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1,accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete account: " + e.getMessage(),e);
        }
    }

    @Override
    public boolean exists(int accountNumber) {
        String sql="SELECT 1 FROM accounts WHERE account_number = ?";
        try (Connection conn=provider.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1,accountNumber);
            try (ResultSet rs=ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to check if account exists: " + e.getMessage(),e);
        }
    }

    @Override
    public int nextAccountNumber() {
        String sql="SELECT COALESCE(MAX(account_number), 1000) + 1 AS next_id FROM accounts";
        try (Connection conn=provider.getConnection();
             Statement stmt=conn.createStatement();
             ResultSet rs=stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
            return 1001;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to generate next account number: " + e.getMessage(),e);
        }
    }

    public IAccount mapRow(ResultSet rs) throws SQLException {
        int accNo=rs.getInt("account_number");
        String name=rs.getString("account_holder_name");
        int age=rs.getInt("age");
        double balance=rs.getDouble("balance");
        String type=rs.getString("account_type");
        String status=rs.getString("status");
        int pin=rs.getInt("pin");

        try {
            Account acc=(Account) AccountFactory.createAccount(type,accNo,name,Math.max(18,age),balance);
            if (pin>0) {
                try {
                    acc.setPin(pin);
                } catch (Exception ignored) {
                }
            }
            if (!"Active".equalsIgnoreCase(status)) {
                try {
                    acc.closeAccount();
                } catch (Exception ignored) {
                }
            }
            return acc;
        } catch (Exception e) {
            throw new SQLException("Error mapping row to Account: " + e.getMessage(),e);
        }
    }
}