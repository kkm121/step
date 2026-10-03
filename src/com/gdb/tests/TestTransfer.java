package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.AccountRulesEngine;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.service.TransferService;

public class TestTransfer {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 15 — TRANSFER WITH DAILY LIMITS");
        System.out.println("============================================================\n");

        AccountRulesEngine.loadRules();

        Account acc1 = (Account) AccountFactory.createAccount("SAVINGS", "1001", "Rajesh Sharma", 30, 100000.0, "Active", "1234", 0);
        Account acc2 = (Account) AccountFactory.createAccount("SAVINGS", "1002", "Priya Patel", 28, 20000.0, "Active", "1234", 0);
        acc1.setPin(1234);

        System.out.println("[STEP 9] " + acc1);
        System.out.println("[STEP 9] " + acc2);

        try {
            TransferService.transfer(acc1, acc2, 5000.0, 1234);
            System.out.println("\n[STEP 10] Transfer Rs. 5,000: SUCCESS | acc1 = Rs. " + acc1.getBalance() + " | acc2 = Rs. " + acc2.getBalance());
        } catch (AccountException e) {
            System.out.println("\n[STEP 10] Transfer failed: " + e.getMessage());
        }

        try {
            TransferService.transfer(acc1, acc2, 100000.0, 1234);
        } catch (InsufficientBalanceException e) {
            System.out.println("[STEP 11] Caught InsufficientBalanceException: " + e.getMessage());
        } catch (AccountException e) {
            System.out.println("[STEP 11] Caught AccountException: " + e.getMessage());
        }

        System.out.println("[STEP 12] Daily limit for acc1: Rs. " + acc1.getDailyTransferLimit());
        int count = 1;
        while (true) {
            try {
                TransferService.transfer(acc1, acc2, 20000.0, 1234);
                System.out.println("  Transfer #" + count + " of Rs. 20,000: SUCCESS | used today = Rs. " + acc1.getDailyTransferTotal());
                count++;
            } catch (AccountException e) {
                System.out.println("[STEP 12] Caught AccountException: " + e.getMessage());
                break;
            }
        }

        System.out.println("[STEP 13] Used today: Rs. " + acc1.getDailyTransferTotal() + " | Remaining: Rs. " + acc1.getRemainingDailyTransferLimit());
    }
}
