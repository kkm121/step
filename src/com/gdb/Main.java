package com.gdb;

import com.gdb.domain.IAccount;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import com.gdb.ui.AccountUI;

public class Main {

    public static void main(String[] args) {
        TransactionLogger logger = new TransactionLogger(new FileLogDestination());
        AccountService service = new AccountService(logger);

        IAccount acc1 = service.openAccount("SAVINGS", "Rajesh Sharma", 30, 25000.0, "1234", 1);
        IAccount acc2 = service.openAccount("CURRENT", "Priya Patel", 28, 50000.0, "1234", 0);

        if (args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            runDemoWorkflow(service);
            return;
        }

        AccountUI ui = new AccountUI(service);
        ui.start();
    }

    private static void runDemoWorkflow(AccountService service) {
        System.out.println("=== Global Digital Bank: System Boot & Demo Workflow ===");
        try {
            service.deposit(1001, 5000.0);
            service.withdraw(1001, 2000.0, 1234);
            service.transfer(1001, 1002, 1000.0, 1234);
            System.out.println("Demo operations executed and persisted to FileLogDestination successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
