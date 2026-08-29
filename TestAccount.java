public class TestAccount {

    private static void printAccount(Account acc) {
        System.out.println("Account #" + acc.getAccountNumber() + " | " + acc.getName() + " (" + acc.getAge() + " years ) | " + acc.getAccountType() + " | Rs " + acc.getBalance() + " | " + acc.getStatus());
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("GLOBAL DIGITAL BANK - ACCOUNT TEST");
        System.out.println("==================================================");
        System.out.println(">>> 1. Creating Account");
        Account acc1 = new Account(1001, "John Doe", 25, 1000.0, "Savings");
        System.out.println("Account created!");
        printAccount(acc1);
        System.out.println(">>> 2. Deposit Money");
        double dep1 = 500.0;
        System.out.println("Depositing Rs " + dep1 + ": " + (acc1.deposit(dep1) ? "SUCCESS\nNew balance: Rs " + acc1.getBalance() : "FAILED (Invalid amount)"));
        double dep2 = -100.0;
        System.out.println("Depositing Rs " + dep2 + ": " + (acc1.deposit(dep2) ? "SUCCESS\nNew balance: Rs " + acc1.getBalance() : "FAILED (Invalid amount)"));
        System.out.println(">>> 3. Withdraw Money");
        double with1 = 200.0;
        System.out.println("Withdrawing Rs " + with1 + ": " + (acc1.withdraw(with1) ? "SUCCESS\nNew balance: Rs " + acc1.getBalance() : "FAILED (Insufficient balance)\nCurrent balance: Rs " + acc1.getBalance()));
        double with2 = 2000.0;
        System.out.println("Withdrawing Rs " + with2 + ": " + (acc1.withdraw(with2) ? "SUCCESS\nNew balance: Rs " + acc1.getBalance() : "FAILED (Insufficient balance)\nCurrent balance: Rs " + acc1.getBalance()));
        System.out.println(">>> 4. Creating Another Account");
        Account acc2 = new Account(1002, "Jane Smith", 30, 2000.0, "Current");
        printAccount(acc2);
        System.out.println(">>> 5. All Accounts");
        printAccount(acc1);
        printAccount(acc2);

        System.out.println("==================================================");
        System.out.println("TEST COMPLETED!");
        System.out.println("==================================================");
    }
}