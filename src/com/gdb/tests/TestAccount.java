package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.exceptions.AccountException;

public class TestAccount {
    public static void main(String[] args) throws AccountException {
        Account account = new Account(1001, "John Doe", 25, 1000.0, "Savings");
        account.setPin(1234);
        account.deposit(500.0);
        account.withdraw(200.0, 1234);
        System.out.println("Account test completed for: " + account.getName());
    }
}
