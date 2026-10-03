package com.gdb.domain;

public final class AccountFactory {
    private AccountFactory() {
    }

    public static IAccount createAccount(String accountType, String accountNumber, String name, int age, double balance, String status, String pin) {
        return createAccount(accountType, accountNumber, name, age, balance, status, pin, 0);
    }

    public static IAccount createAccount(String accountType, String accountNumber, String name, int age, double balance, String status, String pin, int customerTenure) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type is required");
        }
        return switch (accountType.toUpperCase()) {
            case "SAVINGS" -> new SavingsAccount(accountNumber, name, age, balance, status, pin, customerTenure);
            case "CURRENT" -> new CurrentAccount(accountNumber, name, age, balance, status, pin);
            case "FIXED_DEPOSIT" -> new FixedDepositAccount(accountNumber, name, age, balance, status, pin);
            case "SALARY" -> new SalaryAccount(accountNumber, name, age, balance, status, pin);
            default -> throw new IllegalArgumentException("Unknown account type: " + accountType);
        };
    }
}
