public class TestAccountEnhanced {
    private static String pinStatus(Account acc) {
        return acc.getPin() == null ?"No":"Yes";
    }
    private static void displayAccount(Account acc) {
        System.out.println("Account #" + acc.getAccountNumber() + " | " + acc.getName() + " (" + acc.getAge() + " years) | " + acc.getAccountType() + " | Rs " + acc.getBalance() + " | " + acc.getStatus() + " | PIN: " + pinStatus(acc));
    }
    private static void displayAccountLine(Account acc) {
        System.out.print("Account #" + acc.getAccountNumber() + " | " + acc.getName() + " (" + acc.getAge() + " years) | " + acc.getAccountType() + " | Rs " + acc.getBalance() + " | " + acc.getStatus() + " | PIN: " + pinStatus(acc));
    }
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("ENHANCED ACCOUNT TEST (BOOLEAN RETURNS)");
        System.out.println("============================================================");
        System.out.println();
        System.out.println(">>> Test 1: Valid Account Creation");
        Account acc1 = new Account(1001, "John Doe", 25, 1000.0, "Savings");
        displayAccount(acc1);
        System.out.println(">>> Test 2: Invalid Age (under 18)");
        System.out.println("Creating account with age 16");
        Account acc2 = new Account(1002, "Young Kid", 16, 500.0, "Savings");
        System.out.println("Age auto-corrected to: 18");
        displayAccount(acc2);
        System.out.println(">>> Test 3: Invalid Account Type");
        System.out.println("Creating account with type \"Invalid\"");
        Account acc3 = new Account(1003, "Test User", 25, 500.0, "Invalid");
        System.out.println("Account type defaulted to: Savings");
        displayAccount(acc3);
        System.out.println(">>> Test 4: Minimum Balance Enforcement on Creation");
        System.out.println("Creating Savings account with Rs 300 (below minimum)");
        Account acc4 = new Account(1004, "Bob Wilson", 25, 300.0, "Savings");
        System.out.println("Balance auto-corrected to minimum: Rs 500.0");
        displayAccount(acc4);
        System.out.println(">>> Test 5: Withdrawal with Minimum Balance");
        Account acc5 = new Account(1005, "Alice Brown", 30, 1000.0, "Current");
        acc5.setPin(1234);
        System.out.print("Initial: ");
        displayAccountLine(acc5);
        System.out.println();
        boolean withdrawSuccess = acc5.withdraw(200.0);
        System.out.println("Withdrawing Rs 200.0: " + (withdrawSuccess ? "SUCCESS" : "FAILED"));
        if (withdrawSuccess) {
            System.out.println("New balance: Rs " + acc5.getBalance());
        }
        System.out.print("After withdrawal: ");
        displayAccountLine(acc5);
        System.out.println();
        boolean insufficientWithdraw = acc5.withdraw(900.0);
        System.out.println("Withdrawing Rs 900.0 (would leave Rs -100): " + (insufficientWithdraw ? "SUCCESS" : "FAILED (Minimum balance violation)"));
        System.out.println("Current balance: Rs " + acc5.getBalance());
        System.out.println(">>> Test 6: Account Status Management");
        Account acc6 = new Account(1006, "Charlie Green", 35, 2000.0, "Savings");
        System.out.print("Initial: ");
        displayAccountLine(acc6);
        System.out.println();
        boolean closeResult = acc6.closeAccount();
        System.out.println("Closing account: " + (closeResult ? "SUCCESS" : "FAILED"));
        System.out.print("After close: ");
        displayAccountLine(acc6);
        System.out.println();
        boolean depositToClosed = acc6.deposit(500.0);
        System.out.println("Depositing Rs 500.0 to closed account: " + (depositToClosed ? "SUCCESS" : "FAILED (Account inactive)"));
        boolean reopenResult = acc6.reopenAccount();
        System.out.println("Reopening account: " + (reopenResult ? "SUCCESS" : "FAILED"));
        System.out.print("After reopen: ");
        displayAccountLine(acc6);
        System.out.println();   
        System.out.println(">>> Test 7: PIN Protection");
        Account acc7 = new Account(1007, "Diana Prince", 28, 1500.0, "Savings");
        boolean pinSet = acc7.setPin(1234);
        System.out.println("Setting PIN 1234: " + (pinSet ? "SUCCESS" : "FAILED"));

        boolean correctPinWithdraw = acc7.withdraw(200.0, 1234);
        System.out.println("Withdrawing Rs 200.0 with correct PIN (1234): " + (correctPinWithdraw ? "SUCCESS" : "FAILED"));
        if (correctPinWithdraw) {
            System.out.println("New balance: Rs " + acc7.getBalance());
        }

        boolean wrongPinWithdraw = acc7.withdraw(100.0, 9999);
        System.out.println("Withdrawing Rs 100.0 with incorrect PIN (9999): " + (wrongPinWithdraw ? "SUCCESS" : "FAILED (Incorrect PIN)"));

        Account acc8 = new Account(1008, "PIN User", 30, 800.0, "Savings");
        boolean noPinWithdraw = acc8.withdraw(100.0, 1234);
        System.out.println("Withdrawing Rs 100.0 with PIN not set: " + (noPinWithdraw ? "SUCCESS" : "FAILED (PIN not set)"));

        System.out.println(">>> Test 8: All Accounts Summary");
        displayAccount(acc1);
        displayAccount(acc2);
        displayAccount(acc3);
        displayAccount(acc4);
        displayAccount(acc5);
        displayAccount(acc6);
        displayAccount(acc7);

        System.out.println("============================================================");
        System.out.println("ENHANCED TEST COMPLETED!");
        System.out.println("============================================================");
    }
}
