package com.gdb.tests;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.CurrentAccount;
import com.gdb.domain.FixedDepositAccount;
import com.gdb.domain.SavingsAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class TestAbstractAccount {
    public static void main(String[] args) {
        System.out.println("=== Activity 9: Abstract Account & Template Pattern ===");

        AbstractAccount savings = new SavingsAccount(3001, "Savings User", 25, 10000.0);
        savings.setPin(1234);
        try {
            savings.withdraw(2000.0, 1234);
            System.out.println("[Savings] Withdraw 2000: SUCCESS | Balance: Rs " + savings.getBalance());
        } catch (AccountException exception) {
            System.out.println("[Savings] Withdraw 2000: Unexpected " + exception.getClass().getSimpleName() + " [FAIL]");
        }

        try {
            savings.withdraw(8000.0, 1234);
            System.out.println("[Savings] Withdraw below min balance: SUCCESS [FAIL]");
        } catch (MinimumBalanceViolationException exception) {
            System.out.println("[Savings] Withdraw below min balance: Caught MinimumBalanceViolationException [PASS]");
        } catch (AccountException exception) {
            System.out.println("[Savings] Withdraw below min balance: Unexpected " + exception.getClass().getSimpleName() + " [FAIL]");
        }

        AbstractAccount current = new CurrentAccount(3002, "Current User", 30, 10000.0);
        current.setPin(1234);
        try {
            current.withdraw(13000.0, 1234);
            System.out.println("[Current] Overdraft debit: SUCCESS | Balance: Rs " + current.getBalance());
        } catch (AccountException exception) {
            System.out.println("[Current] Overdraft debit: Unexpected " + exception.getClass().getSimpleName() + " [FAIL]");
        }

        AbstractAccount fixedDeposit = new FixedDepositAccount(3003, "Fixed Deposit User", 35, 10000.0, 12, 6.5);
        fixedDeposit.setPin(1234);
        try {
            fixedDeposit.withdraw(1000.0, 1234);
            System.out.println("[FixedDeposit] Premature debit: SUCCESS [FAIL]");
        } catch (AccountException exception) {
            System.out.println("[FixedDeposit] Premature debit: Caught AccountException [PASS]");
        }

        System.out.println("Template method pattern executed successfully!");
    }
}