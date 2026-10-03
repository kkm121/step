package com.gdb.domain;
import com.gdb.exceptions.AccountException;

public class FixedDepositAccount extends AbstractAccount {
    private int tenureMonths;
    private double interestRate;
    public FixedDepositAccount(int accountNumber,String name,int age,double initialBalance,int tenureMonths,double interestRate) {
        super(accountNumber, name, age, initialBalance, "FIXED_DEPOSIT");
        this.tenureMonths=tenureMonths;
        this.interestRate=interestRate;
    }

    public FixedDepositAccount(String accountNumber, String name, int age, double initialBalance,
                               String status, String pin) {
        super(accountNumber, name, age, initialBalance, "FIXED_DEPOSIT", status, pin);
        this.tenureMonths = 12;
        this.interestRate = 6.5;
    }
    public double calculateMaturityAmount() {
        return getBalance()*Math.pow(1+(interestRate/100),tenureMonths/12.0);
    }
    @Override
    protected void processDebit(double amount) throws AccountException {
        throw new AccountException("Premature withdrawals are not permitted on Fixed Deposit accounts before maturity.");
    }
    public int getTenureMonths() {
        return tenureMonths;
    }
    public double getInterestRate() {
        return interestRate;
    }
}
