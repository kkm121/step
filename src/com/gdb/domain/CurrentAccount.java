package com.gdb.domain;

import com.gdb.exceptions.*;

public class CurrentAccount extends AbstractAccount {
    private double overdraftLimit;

    public CurrentAccount(int accountNumber, String name, int age, double initialBalance) {
        super(accountNumber, name, age, initialBalance, "CURRENT");
        this.overdraftLimit = 25000.0;
    }
    @Override
    protected void processDebit(double amount) throws InsufficientBalanceException {
        if (amount > balance + overdraftLimit) {
            throw new InsufficientBalanceException("Insufficient balance. Available with overdraft: Rs " + (balance + overdraftLimit) + ", Requested: Rs " + amount);
        }
        balance -= amount;
    }
    public double getOverdraftLimit() {
        return overdraftLimit;
    }
    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
