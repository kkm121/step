package com.gdb.command;

import com.gdb.domain.Account;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TransferCommand implements TransactionCommand {

    private static final long serialVersionUID = 1L;

    private Account from;
    private Account to;
    private double amount;
    private int pin;
    private Transaction transaction;

    public TransferCommand(Account from, Account to, double amount, int pin) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public void execute() throws Exception {
        this.transaction = TransferService.transferWithTransaction(from, to, amount, pin);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    public Account getFrom() {
        return from;
    }

    public Account getTo() {
        return to;
    }

    public double getAmount() {
        return amount;
    }

    public int getPin() {
        return pin;
    }
}
