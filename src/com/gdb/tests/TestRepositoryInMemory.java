package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.repository.AccountRepository;
import com.gdb.repository.InMemoryAccountRepository;
import com.gdb.repository.InMemoryTransactionRepository;
import com.gdb.repository.RepositoryFactory;
import com.gdb.repository.TransactionRepository;
import com.gdb.service.AccountService;
import java.time.LocalDateTime;
import java.util.List;

public class TestRepositoryInMemory {

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 21 — REPOSITORY PATTERN IN-MEMORY TESTS");
        System.out.println("=".repeat(60));

        AccountRepository accountRepo = new InMemoryAccountRepository();
        TransactionRepository txnRepo = new InMemoryTransactionRepository();
        TransactionLogger logger = new TransactionLogger(new MemoryLogDestination());

        AccountService service = new AccountService(accountRepo, txnRepo, logger);

        System.out.println("[TEST 1] Account Repository CRUD:");
        int accNum1 = accountRepo.nextAccountNumber();
        IAccount a1 = AccountFactory.createAccount("SAVINGS", String.valueOf(accNum1), "Rajesh Sharma", 30, 50000.0, "Active", "1234", 0);
        accountRepo.save(a1);
        System.out.println("  Saved: " + a1);

        IAccount found = accountRepo.findById(1001);
        System.out.println("  Found: " + found);
        assert found != null : "Account 1001 should be found";

        a1.deposit(15000.0);
        accountRepo.update(a1);
        System.out.println("  Updated balance: Rs. " + accountRepo.findById(1001).getBalance());
        assert accountRepo.findById(1001).getBalance() == 65000.0 : "Updated balance should be 65000.0";

        int accNum2 = accountRepo.nextAccountNumber();
        IAccount a2 = AccountFactory.createAccount("CURRENT", String.valueOf(accNum2), "Priya Patel", 28, 40000.0, "Active", "5678", 0);
        accountRepo.save(a2);

        System.out.println("  Total accounts in repository: " + accountRepo.findAll().size());
        assert accountRepo.findAll().size() == 2 : "Total accounts should be 2";
        assert accountRepo.exists(1001) : "Account 1001 should exist";
        assert accountRepo.exists(1002) : "Account 1002 should exist";
        System.out.println("  -> PASSED\n");

        System.out.println("[TEST 2] Transaction Repository Operations:");
        Transaction t1 = new Transaction(Transaction.generateId(), LocalDateTime.now(), 1001, TransactionType.DEPOSIT, 10000.0, 60000.0, "SUCCESS", "Deposit", 0, 1001);
        Transaction t2 = new Transaction(Transaction.generateId(), LocalDateTime.now(), 1001, TransactionType.WITHDRAW, 5000.0, 55000.0, "SUCCESS", "Withdrawal", 1001, 0);
        txnRepo.save(t1);
        txnRepo.save(t2);
        System.out.println("  Saved " + txnRepo.findAll().size() + " transaction records.");
        assert txnRepo.findAll().size() == 2 : "Total transactions should be 2";

        List<Transaction> acc1Txns = txnRepo.findByAccount(1001);
        System.out.println("  Found transactions for #1001: " + acc1Txns.size());
        assert acc1Txns.size() == 2 : "Transactions for 1001 should be 2";
        System.out.println("  -> PASSED\n");

        System.out.println("[TEST 3] Service Integration with Repositories:");
        AccountRulesEngine.loadRules();

        IAccount acc3 = service.openAccount("SAVINGS", "Amit Kumar", 35, 20000.0, 0);
        System.out.println("  Opened account #" + acc3.getAccountNumber() + " via AccountService.");
        assert "1003".equals(acc3.getAccountNumber()) || acc3.getAccountNumberAsInt() == 1003 : "acc3 should have ID 1003";

        Transaction depTxn = service.deposit(1003, 5000.0);
        System.out.println("  Deposit executed. New Balance: Rs. " + service.getAccount(1003).getBalance());
        assert service.getAccount(1003).getBalance() == 25000.0 : "Balance should be 25000.0";
        assert depTxn != null : "Deposit transaction record should not be null";

        Transaction wthTxn = service.withdraw(1003, 3000.0, 1234);
        assert service.getAccount(1003).getBalance() == 22000.0 : "Balance after withdraw should be 22000.0";
        assert wthTxn != null : "Withdraw transaction record should not be null";

        Transaction trfTxn = service.transfer(1001, 1003, 5000.0, 1234);
        assert trfTxn != null : "Transfer transaction should not be null";
        assert service.getAccount(1001).getBalance() == 60000.0 : "Sender balance should be 60000.0";
        assert service.getAccount(1003).getBalance() == 27000.0 : "Receiver balance should be 27000.0";

        assert service.getAllAccounts().size() == 3 : "Total service accounts should be 3";
        assert !service.getTransactionRecords().isEmpty() : "Transaction records should not be empty";

        String mode = RepositoryFactory.getPersistenceMode();
        assert "memory".equals(mode) : "Persistence mode should be memory";
        assert RepositoryFactory.getAccountRepository() != null : "Factory account repo should not be null";
        assert RepositoryFactory.getTransactionRepository() != null : "Factory transaction repo should not be null";

        System.out.println("  -> PASSED\n");

        System.out.println("=".repeat(60));
        System.out.println("  ALL ACTIVITY 21 IN-MEMORY REPOSITORY TESTS PASSED!");
        System.out.println("=".repeat(60));
    }
}
