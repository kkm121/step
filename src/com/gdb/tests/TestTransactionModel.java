package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.AccountRulesEngine;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TestTransactionModel {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 16 — TRANSACTION MODEL TEST");
        System.out.println("============================================================\n");

        AccountRulesEngine.loadRules();

        try {
            Account acc1 = (Account) AccountFactory.createAccount("SAVINGS", "1001", "Rajesh Sharma", 30, 50000.0, "Active", "1234", 0);
            Account acc2 = (Account) AccountFactory.createAccount("SAVINGS", "1002", "Priya Patel", 28, 20000.0, "Active", "1234", 0);
            acc1.setPin(1234);
            Transaction t1 = acc1.depositWithTransaction(5000.0);
            System.out.println("[STEP 10] Deposit Transaction: " + t1);
            Transaction t2 = acc1.withdrawWithTransaction(2000.0, 1234);
            System.out.println("[STEP 11] Withdrawal Transaction: " + t2);
            Transaction t3 = TransferService.transferWithTransaction(acc1, acc2, 1000.0, 1234);
            System.out.println("[STEP 12] Transfer Transaction: " + t3);
            acc1.deposit(1000.0);
            System.out.println("[STEP 13] Legacy Deposit +1000: " + acc1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
