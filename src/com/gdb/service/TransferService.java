package com.gdb.service;

import com.gdb.domain.Account;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.InvalidPinException;
import java.time.LocalDateTime;

public class TransferService {

    public static void transfer(IAccount from, IAccount to, double amount, int pin) throws AccountException {
        if (from == null || to == null) {
            throw new AccountException("Source and destination accounts are required");
        }

        if (!"Active".equalsIgnoreCase(from.getStatus()) || !"Active".equalsIgnoreCase(to.getStatus())) {
            throw new InactiveAccountException("Both accounts must be active to transfer funds");
        }

        if (!from.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }

        if (!from.canWithdraw(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for transfer of Rs. " + amount);
        }

        if (!(from instanceof Account)) {
            throw new AccountException("Source account does not support daily transfer limits");
        }
        Account source = (Account) from;
        source.resetDailyTransferIfNeeded();
        if (!source.canTransfer(amount)) {
            double remaining = source.getRemainingDailyTransferLimit();
            throw new AccountException("Daily transfer limit exceeded. Remaining today: Rs. " + remaining);
        }

        from.withdraw(amount, pin);
        to.deposit(amount);
        source.updateDailyTransferTotal(amount);
    }

    public static void transfer(IAccount from, IAccount to, double amount, String pin) throws AccountException {
        try {
            int numericPin = Integer.parseInt(pin);
            transfer(from, to, amount, numericPin);
        } catch (NumberFormatException e) {
            throw new InvalidPinException("Incorrect PIN");
        }
    }

    public static Transaction transferWithTransaction(IAccount from, IAccount to, double amount, int pin) throws AccountException {
        transfer(from, to, amount, pin);
        int fromNum = 0;
        int toNum = 0;
        try {
            fromNum = Integer.parseInt(from.getAccountNumber().replaceAll("\\D", ""));
        } catch (Exception ignored) {}
        try {
            toNum = Integer.parseInt(to.getAccountNumber().replaceAll("\\D", ""));
        } catch (Exception ignored) {}

        return new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                fromNum,
                TransactionType.TRANSFER,
                amount,
                from.getBalance(),
                "SUCCESS",
                "Transfer of Rs. " + amount + " to Account #" + to.getAccountNumber(),
                fromNum,
                toNum
        );
    }
}
