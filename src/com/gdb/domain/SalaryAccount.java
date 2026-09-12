package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;

public class SalaryAccount extends AbstractAccount {
    private String employerName;
    private int inactiveMonths;

    public SalaryAccount(int accountNumber, String name, int age, double initialBalance, String employerName) {
        super(accountNumber, name, age, initialBalance, "SALARY");
        this.employerName = employerName;
        this.inactiveMonths = 0;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient salary account balance");
        }
        balance -= amount;
    }

    public String getEmployerName() {
        return employerName;
    }

    public int getInactiveMonths() {
        return inactiveMonths;
    }

    public void setInactiveMonths(int inactiveMonths) {
        this.inactiveMonths = inactiveMonths;
    }
}
