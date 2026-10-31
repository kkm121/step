package com.gdb.tests;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.JdbcConnectionProvider;
import com.gdb.db.SchemaInitializer;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestJdbcConnection {

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 22 — JDBC CONNECTION & SCHEMA INITIALIZATION");
        System.out.println("=".repeat(60));

        File testDb=new File("test_gdb.db");
        if (testDb.exists()) {
            testDb.delete();
        }

        String testDbUrl="jdbc:sqlite:test_gdb.db";
        ConnectionProvider provider=new JdbcConnectionProvider(testDbUrl);

        System.out.println("[TEST 1] Database Connection Establishment:");
        try (Connection conn=provider.getConnection()) {
            System.out.println("  Connected to: " + testDbUrl);
            System.out.println("  Driver Name: " + conn.getMetaData().getDriverName());
            assert conn.getMetaData().getDriverName().contains("SQLite"):"Driver name should contain SQLite";
            try (Statement stmt=conn.createStatement();
                 ResultSet rs=stmt.executeQuery("SELECT 1")) {
                assert rs.next():"Query SELECT 1 should return a result";
                assert rs.getInt(1)==1:"Result should be 1";
            }
        }
        System.out.println("  -> PASSED\n");

        System.out.println("[TEST 2] Schema DDL Execution:");
        System.out.println("  Creating tables: accounts, transactions...");
        SchemaInitializer.initialize(provider);
        System.out.println("  Tables initialized successfully.");
        System.out.println("  -> PASSED\n");

        System.out.println("[TEST 3] Schema Verification:");
        boolean accountsExists=false;
        boolean transactionsExists=false;
        try (Connection conn=provider.getConnection();
             Statement stmt=conn.createStatement();
             ResultSet rs=stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'")) {
            while (rs.next()) {
                String tableName=rs.getString("name");
                if ("accounts".equalsIgnoreCase(tableName)) {
                    accountsExists=true;
                }
                if ("transactions".equalsIgnoreCase(tableName)) {
                    transactionsExists=true;
                }
            }
        }
        System.out.println("  Table 'accounts' exists: " + accountsExists);
        System.out.println("  Table 'transactions' exists: " + transactionsExists);
        assert accountsExists:"Table 'accounts' must exist";
        assert transactionsExists:"Table 'transactions' must exist";
        System.out.println("  -> PASSED\n");

        provider.shutdown();

        System.out.println("=".repeat(60));
        System.out.println("  ALL ACTIVITY 22 JDBC CONNECTION TESTS PASSED!");
        System.out.println("=".repeat(60));
    }
}
