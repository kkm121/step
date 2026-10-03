package com.gdb.domain;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class SavingsAccount extends AbstractAccount {

    private double minBalance;
    private double interestRate;

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance) {
        super(accountNumber, name, age, initialBalance, "SAVINGS");
        this.tenureYears = 0;
        this.minBalance = 1000.0;
        this.interestRate = 4.0;
    }

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) {
        super(accountNumber, name, age, initialBalance, "SAVINGS");
        this.tenureYears = tenureYears;
        this.minBalance = AccountRulesEngine.getSavingsMinBalance(tenureYears);
        this.interestRate = AccountRulesEngine.getSavingsInterestRate(tenureYears);
    }

    public SavingsAccount(String accountNumber, String name, int age, double initialBalance, String status, String pin) {
        this(accountNumber, name, age, initialBalance, status, pin, 0);
    }

    public SavingsAccount(String accountNumber, String name, int age, double initialBalance, String status, String pin, int tenureYears) {
        super(accountNumber, name, age, initialBalance, "SAVINGS", status, pin, tenureYears);
        this.tenureYears = tenureYears;
        this.minBalance = AccountRulesEngine.getSavingsMinBalance(tenureYears);
        this.interestRate = AccountRulesEngine.getSavingsInterestRate(tenureYears);
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        if (balance - amount < minBalance) {
            throw new MinimumBalanceViolationException("Withdrawal amount must be greater than minimum balance. Provided: Rs " + amount);
        }
        balance-=amount;
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && (balance - amount >= minBalance);
    }

    public void applyInterest() {
        double interest = getBalance() * (interestRate / 100);
        this.balance += interest;
    }
    public double getMinBalance() {
        return minBalance;
    }
    public double getInterestRate() {
        return interestRate;
    }
}
