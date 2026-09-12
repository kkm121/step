package com.gdb.domain;

import com.gdb.exceptions.AccountException;

public class FixedDepositAccount extends Account {
    private int tenureMonths;
    private double interestRate;

    public FixedDepositAccount(int accountNumber, String name, int age, double initialBalance, int tenureMonths, double interestRate) {
        super(accountNumber, name, age, initialBalance, "FIXED_DEPOSIT");
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    public double calculateMaturityAmount() {
        return getBalance() * Math.pow(1 + (interestRate / 100), tenureMonths / 12.0);
    }

    @Override
    public void withdraw(double amount) throws AccountException {
        throw new AccountException("Premature withdrawals are not permitted on Fixed Deposit accounts before maturity.");
    }

    @Override
    public void withdraw(double amount, int pin) throws AccountException {
        throw new AccountException("Premature withdrawals are not permitted on Fixed Deposit accounts before maturity.");
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }
}
