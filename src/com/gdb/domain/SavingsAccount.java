package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class SavingsAccount extends Account {
    private double minBalance;
    private double interestRate;

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance) {
        super(accountNumber, name, age, initialBalance, "SAVINGS");
        this.minBalance = 1000.0;
        this.interestRate = 4.0;
    }
    @Override
    public void withdraw(double amount) throws AccountException {
        if(super.getBalance() - amount < minBalance){
            throw new MinimumBalanceViolationException("Withdrawal amount must be greater than minimum balance. Provided: Rs " + amount);
        }
        super.withdraw(amount);
    }

    @Override
    public void withdraw(double amount, int pin) throws AccountException {
        if (super.getBalance() - amount < minBalance) {
            throw new MinimumBalanceViolationException("Withdrawal amount must be greater than minimum balance. Provided: Rs " + amount);
        }
        super.withdraw(amount, pin);
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
