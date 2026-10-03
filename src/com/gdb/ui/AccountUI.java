package com.gdb.ui;

import com.gdb.command.TransactionCommand;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.AccountService;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AccountUI {

    private final AccountService accountService;
    private final Scanner scanner;

    public AccountUI(AccountService accountService) {
        this(accountService, new Scanner(System.in));
    }

    public AccountUI(AccountService accountService, Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("Select an option (1-8, 0 to Exit): ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleOpenAccount();
                case "2" -> handleViewAccount();
                case "3" -> handleViewAllAccounts();
                case "4" -> handleDeposit();
                case "5" -> handleWithdraw();
                case "6" -> handleTransfer();
                case "7" -> handleTransactionHistory();
                case "8" -> handleCloseAccount();
                case "0" -> {
                    System.out.println("Thank you for banking with Global Digital Bank. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose between 0 and 8.");
            }
            if (running) {
                System.out.println();
            }
        }
    }

    public void displayMenu() {
        System.out.println("============================================================");
        System.out.println("            GLOBAL DIGITAL BANK — CONSOLE PORTAL            ");
        System.out.println("============================================================");
        System.out.println("  1. Open Account");
        System.out.println("  2. View Account Details");
        System.out.println("  3. List All Accounts");
        System.out.println("  4. Deposit Funds");
        System.out.println("  5. Withdraw Funds");
        System.out.println("  6. Transfer Funds");
        System.out.println("  7. View Transaction History");
        System.out.println("  8. Close Account");
        System.out.println("  0. Exit");
        System.out.println("------------------------------------------------------------");
    }

    private void handleOpenAccount() {
        try {
            System.out.print("Enter Account Type (SAVINGS, CURRENT, SALARY, FIXED_DEPOSIT): ");
            String type = scanner.nextLine().trim();

            System.out.print("Enter Customer Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Initial Deposit (Rs): ");
            double initialDeposit = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Set 4-digit PIN: ");
            String pin = scanner.nextLine().trim();

            System.out.print("Enter Customer Tenure in Years (default 0): ");
            String tenureInput = scanner.nextLine().trim();
            int tenure = tenureInput.isEmpty() ? 0 : Integer.parseInt(tenureInput);

            IAccount account = accountService.openAccount(type, name, age, initialDeposit, pin, tenure);
            System.out.println("SUCCESS: Account opened successfully!");
            System.out.println("  " + account);
        } catch (Exception e) {
            System.out.println("ERROR: Could not open account: " + e.getMessage());
        }
    }

    private void handleViewAccount() {
        try {
            System.out.print("Enter Account Number: ");
            int accNo = Integer.parseInt(scanner.nextLine().trim());
            IAccount account = accountService.getAccount(accNo);
            if (account == null) {
                System.out.println("ERROR: Account not found: " + accNo);
                return;
            }
            System.out.println("Account Details:");
            account.displayAccountInfo();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void handleViewAllAccounts() {
        Collection<IAccount> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts currently registered.");
            return;
        }
        System.out.println("Registered Accounts (" + accounts.size() + "):");
        for (IAccount acc : accounts) {
            System.out.println("  " + acc);
        }
    }

    private void handleDeposit() {
        try {
            System.out.print("Enter Account Number: ");
            int accNo = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Amount to Deposit (Rs): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            Transaction txn = accountService.deposit(accNo, amount);
            System.out.println("SUCCESS: " + txn);
        } catch (Exception e) {
            System.out.println("ERROR: Deposit failed: " + e.getMessage());
        }
    }

    private void handleWithdraw() {
        try {
            System.out.print("Enter Account Number: ");
            int accNo = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Amount to Withdraw (Rs): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter 4-digit PIN: ");
            int pin = Integer.parseInt(scanner.nextLine().trim());

            Transaction txn = accountService.withdraw(accNo, amount, pin);
            System.out.println("SUCCESS: " + txn);
        } catch (Exception e) {
            System.out.println("ERROR: Withdrawal failed: " + e.getMessage());
        }
    }

    private void handleTransfer() {
        try {
            System.out.print("Enter Source Account Number: ");
            int fromAcc = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Destination Account Number: ");
            int toAcc = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Amount to Transfer (Rs): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Source Account PIN: ");
            int pin = Integer.parseInt(scanner.nextLine().trim());

            Transaction txn = accountService.transfer(fromAcc, toAcc, amount, pin);
            System.out.println("SUCCESS: " + txn);
        } catch (Exception e) {
            System.out.println("ERROR: Transfer failed: " + e.getMessage());
        }
    }

    private void handleTransactionHistory() {
        List<TransactionCommand> history = accountService.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            return;
        }
        System.out.println("Transaction History (" + history.size() + " records):");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
        }
    }

    private void handleCloseAccount() {
        try {
            System.out.print("Enter Account Number: ");
            int accNo = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter 4-digit PIN: ");
            int pin = Integer.parseInt(scanner.nextLine().trim());

            accountService.closeAccount(accNo, pin);
            System.out.println("SUCCESS: Account #" + accNo + " has been closed.");
        } catch (Exception e) {
            System.out.println("ERROR: Could not close account: " + e.getMessage());
        }
    }
}
