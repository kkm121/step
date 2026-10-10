package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidAmountException;

public interface IAccount {
    String getAccountNumber();
    String getName();
    double getBalance();
    String getAccountType();
    String getStatus();
    int getTenureYears();

    void deposit(double amount) throws InvalidAmountException, InactiveAccountException;
    void withdraw(double amount, String enteredPin) throws AccountException;
    void withdraw(double amount, int enteredPin) throws AccountException;
    void displayAccountInfo();

    boolean verifyPin(int pin);
    boolean verifyPin(String pin);
    boolean canWithdraw(double amount);

    default int getAccountNumberAsInt() {
        try {
            return Integer.parseInt(getAccountNumber().replaceAll("\\D", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
