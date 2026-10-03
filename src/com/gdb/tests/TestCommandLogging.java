package com.gdb.tests;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.domain.Account;
import com.gdb.domain.AccountFactory;
import com.gdb.logging.TransactionLog;
import java.util.List;

public class TestCommandLogging {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 17 — COMMAND PATTERN + FILE LOGGING");
        System.out.println("============================================================\n");

        TransactionLog log = new TransactionLog();
        log.clear();

        try {
            Account acc1 = (Account) AccountFactory.createAccount("SAVINGS", "1001", "Rajesh Sharma", 30, 15000.0, "Active", "1234", 0);
            Account acc2 = (Account) AccountFactory.createAccount("SAVINGS", "1002", "Priya Patel", 28, 20000.0, "Active", "1234", 0);
            acc1.setPin(1234);

            DepositCommand depCmd = new DepositCommand(acc1, 5000.0);
            depCmd.execute();
            log.log(depCmd);
            System.out.println("[STEP 11] Logged: " + depCmd.getTransaction());

            WithdrawCommand withCmd = new WithdrawCommand(acc1, 2000.0, 1234);
            withCmd.execute();
            log.log(withCmd);
            System.out.println("[STEP 12] Logged: " + withCmd.getTransaction());

            TransferCommand transCmd = new TransferCommand(acc1, acc2, 3000.0, 1234);
            transCmd.execute();
            log.log(transCmd);
            System.out.println("[STEP 13] Logged: " + transCmd.getTransaction());

            List<TransactionCommand> history = log.readAll();
            System.out.println("\n[STEP 14] Read " + history.size() + " commands from transaction log:");
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
            }

            TransactionLog freshLog = new TransactionLog();
            List<TransactionCommand> freshHistory = freshLog.readAll();
            if (freshHistory.size() == 3) {
                System.out.println("\n[STEP 15] Fresh reader verified 3 persisted commands [PASS]");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
