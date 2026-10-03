package com.gdb.domain;

import com.gdb.exceptions.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Account implements IAccount, Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MIN_AGE = 18;
    private static final double MIN_SAVINGS = 500.0;
    private static final double MIN_CURRENT = 1000.0;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    protected String accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected String pin;
    protected int tenureYears = 0;

    protected double dailyTransferTotal = 0.0;
    protected LocalDateTime lastTransferDate = LocalDateTime.now();

    public Account(int accountNumber, String name, int age, double initialBalance, String accountType)
            throws IllegalArgumentException {
        this(String.valueOf(accountNumber), name, age, initialBalance, accountType, "Active", null, 0);
    }

    public Account(String accountNumber, String name, int age, double initialBalance,
                   String accountType, String status, String pin) {
        this(accountNumber, name, age, initialBalance, accountType, status, pin, 0);
    }

    public Account(String accountNumber, String name, int age, double initialBalance,
                   String accountType, String status, String pin, int tenureYears) {
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("Customer must be at least 18 years old. Provided: " + age);
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountNumber = accountNumber != null ? accountNumber : "0";
        this.name = name;
        this.age = age;
        this.accountType = normalizeAccountType(accountType);
        this.status = status != null ? status : "Active";
        this.pin = pin;
        this.tenureYears = Math.max(0, tenureYears);
        this.balance = initialBalance;
    }

    private String normalizeAccountType(String type) {
        if (type == null) return "SAVINGS";
        if ("Savings".equalsIgnoreCase(type) || "SAVINGS".equalsIgnoreCase(type)) {
            return "SAVINGS";
        }
        if ("Current".equalsIgnoreCase(type) || "CURRENT".equalsIgnoreCase(type)) {
            return "CURRENT";
        }
        if ("FixedDeposit".equalsIgnoreCase(type) || "Fixed_Deposit".equalsIgnoreCase(type) || "FIXED_DEPOSIT".equalsIgnoreCase(type)) {
            return "FIXED_DEPOSIT";
        }
        if ("Salary".equalsIgnoreCase(type) || "SALARY".equalsIgnoreCase(type)) {
            return "SALARY";
        }
        return type.toUpperCase();
    }

    private double minbalance(String type) {
        if ("CURRENT".equalsIgnoreCase(type)) {
            return MIN_CURRENT;
        }
        return MIN_SAVINGS;
    }

    protected void validateActive() throws InactiveAccountException {
        if (!"Active".equalsIgnoreCase(status)) {
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
    }

    @Override
    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: Rs " + amount);
        }
        this.balance += amount;
    }

    @Override
    public void withdraw(double amount, String enteredPin) throws AccountException {
        validateActive();
        if (this.pin == null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        if (!verifyPin(enteredPin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: Rs " + amount);
        }
        if (this.balance < amount) {
            throw new InsufficientBalanceException("Insufficient balance. Available: Rs " + this.balance + ", Requested: Rs " + amount);
        }
        this.balance -= amount;
    }

    @Override
    public void withdraw(double amount, int pin) throws AccountException {
        withdraw(amount, String.valueOf(pin));
    }

    public void withdraw(double amount) throws AccountException {
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: Rs " + amount);
        }
        if (this.balance < amount) {
            throw new InsufficientBalanceException("Insufficient balance. Available: Rs " + this.balance + ", Requested: Rs " + amount);
        }
        this.balance -= amount;
    }

    @Override
    public boolean verifyPin(int pin) {
        return verifyPin(String.valueOf(pin));
    }

    @Override
    public boolean verifyPin(String pin) {
        return this.pin != null && this.pin.equals(pin);
    }

    public boolean hasPin() {
        return pin != null;
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && this.balance >= amount;
    }

    public void resetDailyTransferIfNeeded() {
        if (lastTransferDate == null || !lastTransferDate.toLocalDate().equals(LocalDate.now())) {
            this.dailyTransferTotal = 0.0;
            this.lastTransferDate = LocalDateTime.now();
        }
    }

    public double getDailyTransferLimit() {
        return AccountRulesEngine.getInstance().getDailyTransferLimit(getAccountType(), getTenureYears());
    }

    public double getRemainingDailyTransferLimit() {
        resetDailyTransferIfNeeded();
        return Math.max(0.0, getDailyTransferLimit() - dailyTransferTotal);
    }

    public boolean canTransfer(double amount) {
        resetDailyTransferIfNeeded();
        return (dailyTransferTotal + amount) <= getDailyTransferLimit();
    }

    public void updateDailyTransferTotal(double amount) {
        resetDailyTransferIfNeeded();
        this.dailyTransferTotal += amount;
        this.lastTransferDate = LocalDateTime.now();
    }

    public double getDailyTransferTotal() {
        return dailyTransferTotal;
    }

    public LocalDateTime getLastTransferDate() {
        return lastTransferDate;
    }

    public Transaction depositWithTransaction(double amount) throws AccountException {
        deposit(amount);
        int accNum = parseAccountNumberAsInt();
        return new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                accNum,
                TransactionType.DEPOSIT,
                amount,
                this.balance,
                "SUCCESS",
                "Deposit of Rs. " + amount,
                0,
                accNum
        );
    }

    public Transaction withdrawWithTransaction(double amount, int pin) throws AccountException {
        withdraw(amount, pin);
        int accNum = parseAccountNumberAsInt();
        return new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                accNum,
                TransactionType.WITHDRAW,
                amount,
                this.balance,
                "SUCCESS",
                "Withdrawal of Rs. " + amount,
                accNum,
                0
        );
    }

    private int parseAccountNumberAsInt() {
        try {
            return Integer.parseInt(accountNumber.replaceAll("\\D", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public void closeAccount() throws IllegalStateException {
        if ("Inactive".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Account is already inactive");
        }
        status = "Inactive";
    }

    public void reopenAccount() throws IllegalStateException {
        if ("Active".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Account is already active");
        }
        status = "Active";
    }

    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException("Invalid PIN");
        }
        this.pin = String.valueOf(pin);
    }

    public void setPin(String pin) throws IllegalArgumentException {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must contain 4 digits");
        }
        this.pin = pin;
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    public int getAccountNumberAsInt() {
        return parseAccountNumberAsInt();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) throws IllegalArgumentException {
        this.accountType = normalizeAccountType(accountType);
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getTenureYears() {
        return tenureYears;
    }

    public void setTenureYears(int tenureYears) {
        this.tenureYears = tenureYears;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Type: " + accountType);
        System.out.println("Status: " + status);
    }

    @Override
    public String toString() {
        String typeLabel = accountType;
        if (typeLabel != null && !typeLabel.isEmpty()) {
            typeLabel = typeLabel.substring(0, 1).toUpperCase() + typeLabel.substring(1).toLowerCase();
        }
        return "Account #" + accountNumber + " | " + name + " (" + age + " yrs, Tenure: " + tenureYears + " yrs) | "
                + typeLabel + " | Rs. " + balance + " | " + status;
    }
}