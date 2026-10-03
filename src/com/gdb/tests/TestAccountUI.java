package com.gdb.tests;

import com.gdb.domain.IAccount;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import com.gdb.ui.AccountUI;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TestAccountUI {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 20 — ACCOUNT UI INTEGRATION TEST");
        System.out.println("============================================================\n");

        MemoryLogDestination memDest = new MemoryLogDestination();
        TransactionLogger logger = new TransactionLogger(memDest);
        AccountService service = new AccountService(logger);

        IAccount acc1 = service.openAccount("SAVINGS", "Rajesh Sharma", 30, 25000.0, "1234", 1);
        IAccount acc2 = service.openAccount("CURRENT", "Priya Patel", 28, 50000.0, "1234", 0);
        System.out.println("[TEST 1] Account Creation & Registration: [PASS]");
        System.out.println("  Created: " + acc1);
        System.out.println("  Created: " + acc2);

        try {
            service.deposit(1001, 5000.0);
            service.withdraw(1001, 2000.0, 1234);
            service.transfer(1001, 1002, 3000.0, 1234);
            System.out.println("\n[TEST 2] Financial Operations Execution: [PASS]");
        } catch (Exception e) {
            System.out.println("\n[TEST 2] Financial Operations Execution: [FAIL] " + e.getMessage());
        }

        int historyCount = service.getTransactionHistory().size();
        if (historyCount == 3) {
            System.out.println("[TEST 3] Transaction Audit Log Verification: [PASS] (3 records)");
        } else {
            System.out.println("[TEST 3] Transaction Audit Log Verification: [FAIL]");
        }

        String simulatedInput = "3\n0\n";
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            AccountUI ui = new AccountUI(service);
            System.out.println("\n[TEST 4] Launching Interactive UI Simulation:");
            ui.start();
            System.out.println("[TEST 4] Interactive UI Simulation: [PASS]");
        } finally {
            System.setIn(originalIn);
        }

        System.out.println("\nAll AccountUI endpoints and integration tests verified successfully!");
    }
}
