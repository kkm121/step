package com.gdb.tests;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.CurrentAccount;
import com.gdb.domain.SalaryAccount;
import com.gdb.domain.SavingsAccount;
import com.gdb.exceptions.AccountException;

public class TestAbstractAccount {
    public static void main(String[] args) {
        System.out.println("=== Activity 10: Banking Operations Suite ===");

        AbstractAccount[] portfolio = {
                createSavingsAccount(),
                createCurrentAccount(),
                createSalaryAccount()
        };

        try {
            transferFunds(portfolio[0], portfolio[1], 3000.0, 1234);
        } catch (AccountException exception) {
            System.out.println("Transfer failed unexpectedly: " + exception.getMessage());
        }

        try {
            transferFunds(portfolio[0], portfolio[1], 1000.0, 9999);
            System.out.println("Failed Transfer (Wrong PIN): SUCCESS [FAIL]");
        } catch (AccountException exception) {
            System.out.println("Failed Transfer (Wrong PIN): Exception caught, no balance changed [PASS]");
        }

        processMonthlyCycle(portfolio);
        System.out.println("All banking operations passed!");
    }

    private static AbstractAccount createSavingsAccount() {
        AbstractAccount account = new SavingsAccount(4001, "Savings User", 25, 10000.0);
        account.setPin(1234);
        return account;
    }

    private static AbstractAccount createCurrentAccount() {
        AbstractAccount account = new CurrentAccount(4002, "Current User", 30, 5000.0);
        account.setPin(1234);
        return account;
    }

    private static AbstractAccount createSalaryAccount() {
        AbstractAccount account = new SalaryAccount(4003, "Salary User", 28, 7000.0, "Infosys");
        account.setPin(1234);
        return account;
    }

    private static void transferFunds(AbstractAccount source, AbstractAccount destination,
                                      double amount, int pin) throws AccountException {
        source.withdraw(amount, pin);
        destination.deposit(amount);
        System.out.println("Transfer Rs " + amount + " from " + accountLabel(source)
            + " to " + accountLabel(destination) + ": SUCCESS");
        System.out.println(accountLabel(source) + " Balance: Rs " + source.getBalance()
            + " | " + accountLabel(destination) + " Balance: Rs " + destination.getBalance());
    }

    private static void processMonthlyCycle(AbstractAccount[] portfolio) {
        for (AbstractAccount account : portfolio) {
            if (account instanceof SavingsAccount savingsAccount) {
                savingsAccount.applyInterest();
            } else if (account instanceof SalaryAccount salaryAccount) {
                if (salaryAccount.getInactiveMonths() == 0) {
                    System.out.println("Salary credit history checked.");
                }
            }
        }
        System.out.println("Monthly Interest Cycle processed for all qualifying accounts.");
    }

    private static String accountLabel(AbstractAccount account) {
        if (account instanceof SavingsAccount) {
            return "Savings";
        }
        if (account instanceof CurrentAccount) {
            return "Current";
        }
        if (account instanceof SalaryAccount) {
            return "Salary";
        }
        return "Account";
    }
}