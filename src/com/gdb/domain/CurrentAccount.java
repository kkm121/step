package com.gdb.domain;

import com.gdb.exceptions.*;

public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(int accountNumber, String name, int age, double initialBalance) {
        super(accountNumber, name, age, initialBalance, "CURRENT");
        this.overdraftLimit = 25000.0;
    }
    @Override
    public void withdraw(double amount) throws InactiveAccountException, InvalidAmountException, InsufficientBalanceException {
        withdrawAmount(amount);
    }

    @Override
    public void withdraw(double amount, int pin) throws InactiveAccountException, InvalidPinException, InvalidAmountException, InsufficientBalanceException {
        validateActive();
        if (!hasPin() || !verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        withdrawAmount(amount);
    }

    private void withdrawAmount(double amount) throws InactiveAccountException, InvalidAmountException, InsufficientBalanceException {
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: Rs " + amount);
        }
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
