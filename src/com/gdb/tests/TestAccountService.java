package com.gdb.tests;

import com.gdb.command.TransactionCommand;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import java.util.List;

public class TestAccountService {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 19 — ACCOUNT SERVICE DEMO");
        System.out.println("============================================================\n");

        MemoryLogDestination memDest = new MemoryLogDestination();
        TransactionLogger logger = new TransactionLogger(memDest);
        AccountService service = new AccountService(logger);

        try {
            IAccount acc1 = service.openAccount("SAVINGS", "John Doe", 25, 15000.0);
            IAccount acc2 = service.openAccount("SAVINGS", "Jane Smith", 30, 10000.0);

            System.out.println("[STEP 13] Opened: " + acc1);
            System.out.println("[STEP 13] Opened: " + acc2);

            Transaction t1 = service.deposit(1001, 5000.0);
            System.out.println("\n[STEP 14] Deposit: " + t1);

            Transaction t2 = service.withdraw(1001, 2000.0, 1234);
            System.out.println("[STEP 15] Withdrawal: " + t2);

            Transaction t3 = service.transfer(1001, 1002, 1000.0, 1234);
            System.out.println("[STEP 16] Transfer: " + t3);

            System.out.println("\n[STEP 17] Final Balances:");
            System.out.println("  John (Account #1001): Rs. " + service.getAccount(1001).getBalance());
            System.out.println("  Jane (Account #1002): Rs. " + service.getAccount(1002).getBalance());

            List<TransactionCommand> history = service.getTransactionHistory();
            System.out.println("\n[STEP 18] Transaction History (" + history.size() + " records):");
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
            }

            System.out.println("\n[STEP 19] Error Handling Checks:");
            try {
                service.deposit(9999, 1000.0);
            } catch (AccountException e) {
                System.out.println("  Deposit to missing account caught: " + e.getMessage() + " [PASS]");
            }

            try {
                service.withdraw(1001, 500.0, 9999);
            } catch (InvalidPinException e) {
                System.out.println("  Withdrawal with wrong PIN caught: " + e.getMessage() + " [PASS]");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
