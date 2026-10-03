package com.gdb.tests;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;
public class TestInterfaceFactory {
    public static void main(String[] args) {
        System.out.println("=== Activity 12: Factory-Driven System Suite ===");
        try {
            IAccount savings = AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 10000.0, "ACTIVE", "1234");
            savings.deposit(500.0);
            try {
                savings.withdraw(9600.0, "1234");
                System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL]");
            } catch (MinimumBalanceViolationException exception) {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [PASS]");
            }
            IAccount current = AccountFactory.createAccount("CURRENT", "CUR1001", "Priya Patel", 34,10000.0, "ACTIVE", "5678");
            current.withdraw(15000.0, "5678");
            System.out.println("[Test 2] Current Account Overdraft Withdrawal: [PASS]");
            IAccount fixedDeposit = AccountFactory.createAccount("FIXED_DEPOSIT", "FD1001", "Amit Kumar", 45,50000.0, "ACTIVE", "1111");
            try {
                fixedDeposit.withdraw(1000.0, "1111");
                System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]");
            } catch (AccountException exception) {
                System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]");
            }
            AccountFactory.createAccount(
                    "SALARY", "SAL1001", "Sneha Verma", 26,
                    30000.0, "ACTIVE", "2222");
            try {
                AccountFactory.createAccount(
                        "UNKNOWN", "UNK1001", "Unknown", 30,
                        1000.0, "ACTIVE", "3333");
                System.out.println("[Test 4] Invalid Type Rejection: [FAIL]");
            } catch (IllegalArgumentException exception) {
                System.out.println("[Test 4] Invalid Type Rejection: [PASS]");
            }
            System.out.println("Factory-driven architecture successfully verified!");
        } catch (Exception exception) {
            System.out.println("Unexpected failure: " + exception.getMessage());
        }
    }
}
