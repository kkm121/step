package com.gdb.tests;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.logging.DatabaseLogDestination;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.LogDestination;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.SimulatedDatabase;
import com.gdb.logging.TransactionLogger;

public class TestBridgeLogging {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 18 — BRIDGE PATTERN (FILE + DB)");
        System.out.println("============================================================\n");

        LogDestination fileDest = new FileLogDestination();
        fileDest.clear();

        SimulatedDatabase db = new SimulatedDatabase();
        LogDestination dbDest = new DatabaseLogDestination(db);
        dbDest.clear();

        LogDestination memDest = new MemoryLogDestination();
        memDest.clear();

        TransactionLogger logger = new TransactionLogger(fileDest);

        try {
            Account acc1 = (Account) AccountFactory.createAccount("SAVINGS", "1001", "Rajesh Sharma", 30, 50000.0, "Active", "1234", 0);
            Account acc2 = (Account) AccountFactory.createAccount("SAVINGS", "1002", "Priya Patel", 28, 20000.0, "Active", "1234", 0);
            acc1.setPin(1234);

            System.out.println("[STEP 25] Logging to FILE destination...");
            logSampleTransactions(logger, acc1, acc2);
            System.out.println("  FILE log count: " + logger.readAll().size());

            System.out.println("\n[STEP 26] Switched to DATABASE destination...");
            logger.setDestination(dbDest);
            logSampleTransactions(logger, acc1, acc2);
            System.out.println("  DATABASE log count: " + logger.readAll().size());

            System.out.println("\n[STEP 27] Switched to MEMORY destination...");
            logger.setDestination(memDest);
            logSampleTransactions(logger, acc1, acc2);
            System.out.println("  MEMORY log count: " + logger.readAll().size());

            System.out.println("\n[STEP 28] Verifying Data Isolation:");
            int fileCount = fileDest.readAll().size();
            int dbCount = dbDest.readAll().size();
            int memCount = memDest.readAll().size();

            System.out.println("  FILE count: " + fileCount + " [EXPECTED: 3]");
            System.out.println("  DATABASE count: " + dbCount + " [EXPECTED: 3]");
            System.out.println("  MEMORY count: " + memCount + " [EXPECTED: 3]");

            if (fileCount == 3 && dbCount == 3 && memCount == 3) {
                System.out.println("\n[STEP 29] All Bridge Pattern log backends verified successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logSampleTransactions(TransactionLogger logger, Account acc1, Account acc2) throws Exception {
        DepositCommand dep = new DepositCommand(acc1, 500.0);
        dep.execute();
        logger.log(dep);

        WithdrawCommand with = new WithdrawCommand(acc1, 200.0, 1234);
        with.execute();
        logger.log(with);

        TransferCommand trans = new TransferCommand(acc1, acc2, 100.0, 1234);
        trans.execute();
        logger.log(trans);
    }
}
