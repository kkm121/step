package com.gdb.domain;
import com.gdb.exceptions.*;

public abstract class AbstractAccount {
    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected Integer pin;
    public AbstractAccount(int accountNumber, String name, int age, double initialBalance, String accountType) {
        if (age < 18) {
            throw new IllegalArgumentException("Customer must be at least 18 years old. Provided: " + age);
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }
    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        validateActive();
        if(amount<=0){
            throw new InvalidAmountException("Deposit amount must be positive. Provided: Rs " + amount);
        }
        this.balance+=amount;
    }
    protected void validatePin(int pin) throws InvalidPinException{
        if(this.pin==null || !this.pin.equals(pin)){
            throw new InvalidPinException("Invalid PIN");
        }
    }
    public void changePin(int oldPin, int newPin) throws InvalidPinException, IllegalArgumentException {
        validatePin(oldPin);
        if(newPin<1000 || newPin>9999){
            throw new IllegalArgumentException("PIN must contain 4 digits");
        }
        this.pin=newPin;
    }

    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < 1000 || pin > 9999) {
            throw new IllegalArgumentException("PIN must contain 4 digits");
        }
        this.pin = pin;
    }
    protected void validateActive() throws InactiveAccountException{
        if(!"Active".equalsIgnoreCase(status)){
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
    }
    public void displayAccountInfo(){
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Type: " + accountType);
        System.out.println("Status: " + status);
    }

    public double getBalance() {
        return balance;
    }
    public final void withdraw(double amount, int enteredPin) throws AccountException {
        validatePin(enteredPin);
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        processDebit(amount);
    }
    protected abstract void processDebit(double amount) throws AccountException;
}