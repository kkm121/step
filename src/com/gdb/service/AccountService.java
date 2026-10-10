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
import com.gdb.repository.AccountRepository;
import com.gdb.repository.RepositoryFactory;
import com.gdb.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionLogger logger;
    private final TransferService transferService;

    public AccountService() {
        this(RepositoryFactory.getAccountRepository(), RepositoryFactory.getTransactionRepository(), new TransactionLogger(new FileLogDestination()), new TransferService());
    }

    public AccountService(TransactionLogger logger) {
        this(RepositoryFactory.getAccountRepository(), RepositoryFactory.getTransactionRepository(), logger, new TransferService());
    }

    public AccountService(TransactionLogger logger, TransferService transferService) {
        this(RepositoryFactory.getAccountRepository(), RepositoryFactory.getTransactionRepository(), logger, transferService);
    }

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionLogger logger) {
        this(accountRepository, transactionRepository, logger, new TransferService());
    }

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionLogger logger, TransferService transferService) {
        this.accountRepository = accountRepository != null ? accountRepository : RepositoryFactory.getAccountRepository();
        this.transactionRepository = transactionRepository != null ? transactionRepository : RepositoryFactory.getTransactionRepository();
        this.logger = logger;
        this.transferService = transferService != null ? transferService : new TransferService();
    }

    public IAccount openAccount(String type, String name, int age, double initialBalance) {
        return openAccount(type, name, age, initialBalance, "1234", 0);
    }

    public IAccount openAccount(String type, String name, int age, double initialBalance, int tenureYears) {
        return openAccount(type, name, age, initialBalance, "1234", tenureYears);
    }

    public IAccount openAccount(String type, String name, int age, double initialBalance, String pin, int tenure) {
        int accNo = accountRepository.nextAccountNumber();
        IAccount account = AccountFactory.createAccount(type, String.valueOf(accNo), name, age, initialBalance, "Active", pin, tenure);
        if (account instanceof Account acc) {
            try {
                acc.setPin(pin);
            } catch (Exception ignored) {
            }
        }
        accountRepository.save(account);
        return account;
    }

    public IAccount getAccount(int accountNumber) {
        return accountRepository.findById(accountNumber);
    }

    public Transaction deposit(int accountNumber, double amount) throws Exception {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        DepositCommand cmd = new DepositCommand((Account) account, amount);
        cmd.execute();
        if (accountRepository != null) {
            accountRepository.update(account);
        }
        if (transactionRepository != null && cmd.getTransaction() != null) {
            transactionRepository.save(cmd.getTransaction());
        }
        if (logger != null) {
            logger.log(cmd);
        }
        return cmd.getTransaction();
    }

    public Transaction withdraw(int accountNumber, double amount, int pin) throws Exception {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        WithdrawCommand cmd = new WithdrawCommand((Account) account, amount, pin);
        cmd.execute();
        if (accountRepository != null) {
            accountRepository.update(account);
        }
        if (transactionRepository != null && cmd.getTransaction() != null) {
            transactionRepository.save(cmd.getTransaction());
        }
        if (logger != null) {
            logger.log(cmd);
        }
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
        if (accountRepository != null) {
            accountRepository.update(from);
            accountRepository.update(to);
        }
        if (transactionRepository != null && cmd.getTransaction() != null) {
            transactionRepository.save(cmd.getTransaction());
        }
        if (logger != null) {
            logger.log(cmd);
        }
        return cmd.getTransaction();
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
        if (accountRepository != null) {
            accountRepository.update(account);
        }
    }

    public List<IAccount> getAllAccounts() {
        return accountRepository != null ? accountRepository.findAll() : Collections.emptyList();
    }

    public List<TransactionCommand> getTransactionHistory() {
        return logger != null ? logger.readAll() : new ArrayList<>();
    }

    public List<Transaction> getTransactionRecords() {
        return transactionRepository != null ? transactionRepository.findAll() : Collections.emptyList();
    }

    public List<Transaction> getTransactionRecords(int accountNumber) {
        return transactionRepository != null ? transactionRepository.findByAccount(accountNumber) : Collections.emptyList();
    }

    public int getNextAccountNumber() {
        return accountRepository.nextAccountNumber();
    }

    public TransactionLogger getLogger() {
        return logger;
    }

    public TransferService getTransferService() {
        return transferService;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }
}
