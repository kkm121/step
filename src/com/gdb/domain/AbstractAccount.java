package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.InvalidPinException;

public abstract class AbstractAccount extends Account {

    public AbstractAccount(int accountNumber, String name, int age, double initialBalance, String accountType) {
        super(String.valueOf(accountNumber), name, age, initialBalance, accountType, "Active", null, 0);
    }

    public AbstractAccount(String accountNumber, String name, int age, double initialBalance,
                           String accountType, String status, String pin) {
        super(accountNumber, name, age, initialBalance, accountType, status, pin, 0);
    }

    public AbstractAccount(String accountNumber, String name, int age, double initialBalance,
                           String accountType, String status, String pin, int tenureYears) {
        super(accountNumber, name, age, initialBalance, accountType, status, pin, tenureYears);
    }

    @Override
    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: Rs " + amount);
        }
        this.balance += amount;
    }

    protected void validatePin(int pin) throws InvalidPinException {
        validatePin(String.valueOf(pin));
    }

    protected void validatePin(String pin) throws InvalidPinException {
        if (this.pin == null || !this.pin.equals(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
    }

    public void changePin(int oldPin, int newPin) throws InvalidPinException, IllegalArgumentException {
        validatePin(oldPin);
        if (newPin < 1000 || newPin > 9999) {
            throw new IllegalArgumentException("PIN must contain 4 digits");
        }
        this.pin = String.valueOf(newPin);
    }

    @Override
    public final void withdraw(double amount, String enteredPin) throws AccountException {
        validatePin(enteredPin);
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        processDebit(amount);
    }

    @Override
    public final void withdraw(double amount, int enteredPin) throws AccountException {
        withdraw(amount, String.valueOf(enteredPin));
    }

    protected abstract void processDebit(double amount) throws AccountException;
}