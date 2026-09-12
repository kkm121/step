package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class testactivity8 {
    public static void main(String[] args) {
        System.out.println("=== Activity 8: Polymorphism Test ===");
        AbstractAccount savings = new SavingsAccount(2001, "Savings User", 25, 10000.0);
        savings.setPin(1234);
        try {
            savings.withdraw(9500.0, 1234);
            System.out.println("[Savings] Withdraw 9500 (breaches min balance 1000): SUCCESS [FAIL]");
        } catch (MinimumBalanceViolationException exception) {
            System.out.println("[Savings] Withdraw 9500 (breaches min balance 1000): Caught MinimumBalanceViolationException [PASS]");
        } catch (AccountException exception) {
            System.out.println("[Savings] Unexpected exception: " + exception.getClass().getSimpleName() + " [FAIL]");
        }

        AbstractAccount current = new CurrentAccount(2002, "Current User", 30, 10000.0);
        current.setPin(1234);
        try {
            current.withdraw(15000.0, 1234);
            System.out.println("[Current] Withdraw with Overdraft (Balance goes to -5000): SUCCESS [PASS]");
        } catch (AccountException exception) {
            System.out.println("[Current] Withdraw with Overdraft: Caught " + exception.getClass().getSimpleName() + " [FAIL]");
        }

        try {
            current.withdraw(20001.0, 1234);
            System.out.println("[Current] Withdraw exceeding Overdraft (exceeds -25000): SUCCESS [FAIL]");
        } catch (InsufficientBalanceException exception) {
            System.out.println("[Current] Withdraw exceeding Overdraft (exceeds -25000): Caught InsufficientBalanceException [PASS]");
        } catch (AccountException exception) {
            System.out.println("[Current] Unexpected exception: " + exception.getClass().getSimpleName() + " [FAIL]");
        }

        AbstractAccount fixedDeposit = new FixedDepositAccount(2003, "Fixed Deposit User", 35, 10000.0, 12, 6.5);
        fixedDeposit.setPin(1234);
        try {
            fixedDeposit.withdraw(1000.0, 1234);
            System.out.println("[FixedDeposit] Withdraw attempt: SUCCESS [FAIL]");
        } catch (AccountException exception) {
            System.out.println("[FixedDeposit] Withdraw attempt: Caught AccountException [PASS]");
        }

        System.out.println("All polymorphic behaviors verified!");
    }
}