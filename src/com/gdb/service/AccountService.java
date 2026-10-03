package com.gdb.service;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AccountService {

    private final Map<Integer, IAccount> accounts;
    private final TransactionLogger logger;
    private final TransferService transferService;
    private int nextAccountNumber = 1001;

    public AccountService() {
        this(new TransactionLogger(new FileLogDestination()));
    }

    public AccountService(TransactionLogger logger) {
        this(logger, new TransferService());
    }

    public AccountService(TransactionLogger logger, TransferService transferService) {
        this.accounts = new LinkedHashMap<>();
        this.logger = logger;
        this.transferService = transferService;
    }

    public IAccount openAccount(String type, String name, int age, double initialBalance) {
        return openAccount(type, name, age, initialBalance, "1234", 0);
    }

    public IAccount openAccount(String type, String name, int age, double initialBalance, String pin, int tenure) {
        int accNo = nextAccountNumber++;
        IAccount account = AccountFactory.createAccount(type, String.valueOf(accNo), name, age, initialBalance, "Active", pin, tenure);
        if (account instanceof Account acc) {
            try {
                acc.setPin(pin);
            } catch (Exception ignored) {}
        }
        accounts.put(accNo, account);
        return account;
    }

    public void closeAccount(int accountNumber, int pin) throws AccountException {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        if (!account.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        if (account instanceof Account acc) {
            acc.closeAccount();
        }
    }

    public Transaction deposit(int accountNumber, double amount) throws Exception {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        DepositCommand cmd = new DepositCommand((Account) account, amount);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    public Transaction withdraw(int accountNumber, double amount, int pin) throws Exception {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        WithdrawCommand cmd = new WithdrawCommand((Account) account, amount, pin);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    public Transaction transfer(int fromAcc, int toAcc, double amount, int pin) throws Exception {
        IAccount from = getAccount(fromAcc);
        if (from == null) {
            throw new AccountException("Account not found: " + fromAcc);
        }
        IAccount to = getAccount(toAcc);
        if (to == null) {
            throw new AccountException("Account not found: " + toAcc);
        }
        TransferCommand cmd = new TransferCommand((Account) from, (Account) to, amount, pin);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    public IAccount getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public Collection<IAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<TransactionCommand> getTransactionHistory() {
        return logger.readAll();
    }

    public int getNextAccountNumber() {
        return nextAccountNumber;
    }

    public TransactionLogger getLogger() {
        return logger;
    }

    public TransferService getTransferService() {
        return transferService;
    }
}
