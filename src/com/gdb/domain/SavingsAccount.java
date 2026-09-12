package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class SavingsAccount extends AbstractAccount {
    private double minBalance;
    private double interestRate;

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance) {
        super(accountNumber, name, age, initialBalance, "SAVINGS");
        this.minBalance = 1000.0;
        this.interestRate = 4.0;
    }
    @Override
    protected void processDebit(double amount) throws AccountException {
        if (balance - amount < minBalance) {
            throw new MinimumBalanceViolationException("Withdrawal amount must be greater than minimum balance. Provided: Rs " + amount);
        }
        balance -= amount;
    }
        
    public void applyInterest() {
        double interest = getBalance() * (interestRate / 100);
        this.balance+=interest;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }
}
