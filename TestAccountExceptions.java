public class TestAccountExceptions {
    private static String pinStatus(Account acc) {
        return acc.getPin() == null ?"No":"Yes";
    }
    private static String displayAccountLine(Account acc) {
        return "Account #" + acc.getAccountNumber() + " | " + acc.getName() + " (" + acc.getAge() + " yrs) | " + acc.getAccountType() + " | Rs " + acc.getBalance() + " | " + acc.getStatus() + " | PIN: " + pinStatus(acc);
    }
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("ACCOUNT TEST WITH EXCEPTIONS");
        System.out.println("============================================================");
        Account acc1=null;
        System.out.println(">>> Test 1: Valid Account Creation");
        try{
            acc1=new Account(1001,"John Doe",25,1000.0,"Savings");
            System.out.println("SUCCESS: "+ displayAccountLine(acc1));
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println(">>> Test 2: Invalid Age (under 18)");
        try{
            Account acc2=new Account(1002,"Young Kid",16,500.0,"Savings");
            System.out.println("SUCCESS: "+ displayAccountLine(acc2));
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println(">>> Test 3: Invalid Account Type");
        try{
            Account acc3=new Account(1003,"Test User",25,500.0,"Invalid");
            System.out.println("SUCCESS: "+ displayAccountLine(acc3));
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println(">>> Test 4: Minimum Balance on Creation");
        try{
            System.out.println("Creating Savings account with 300");
            Account acc4=new Account(1004,"Bob Wilson",25,300.0,"Savings");
            System.out.println("SUCCESS: "+ displayAccountLine(acc4));
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        Account acc5=null;
        System.out.println(">>> Test 5: Valid Deposit and Withdrawal");
        try{
            acc5=new Account(1005,"Alice Brown",30,1000.0,"Current");
            System.out.println("Account: "+ displayAccountLine(acc5));
            acc5.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");
            acc5.deposit(500.0);
            System.out.println("Depositing Rs 500.0: SUCCESS");
            System.out.println("Balance after deposit: Rs "+ acc5.getBalance());
            acc5.withdraw(200.0,1234);
            System.out.println("Withdrawing Rs 200.0: SUCCESS");
            System.out.println("Balance after withdrawal: Rs "+ acc5.getBalance());
            System.out.println(displayAccountLine(acc5));
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println(">>> Test 6: Invalid Deposit (Negative Amount)");
        try{
            System.out.println("Attempting to deposit Rs -100.0");
            acc5.deposit(-100.0);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        Account acc6=null;
        System.out.println(">>> Test 7: Insufficient Balance");
        try{
            acc6=new Account(1006,"Charlie Green",35,500.0,"Savings");
            acc6.setPin(1111);
            System.out.println("Account: "+ displayAccountLine(acc6));
            System.out.println("Attempting to withdraw Rs 1000.0");
            acc6.withdraw(1000.0,1111);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " +e.getMessage());
        }
        Account acc7=null;
        System.out.println(">>> Test 8: Minimum Balance Violation");
        try{
            acc7=new Account(1007,"Diana Prince",28,1000.0,"Savings");
            acc7.setPin(2222);
            System.out.println("Account: "+displayAccountLine(acc7));
            System.out.println("Attempting to withdraw Rs 600.0");
            acc7.withdraw(600.0,2222);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " +e.getMessage());
        }
        Account acc8=null;
        System.out.println(">>> Test 9: Inactive Account Operations");
        try{
            acc8=new Account(1008,"Eve Wilson",32,2000.0,"Current");
            System.out.println("Account: "+displayAccountLine(acc8));
            acc8.closeAccount();
            System.out.println("Closing account: SUCCESS");
            System.out.println("Attempting to deposit Rs 100.0 on closed account");
            acc8.deposit(100.0);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        try{
            acc8.reopenAccount();
            System.out.println("Reopening account: SUCCESS");
            acc8.deposit(100.0);
            System.out.println("Depositing 100.0 after reopen: SUCCESS");
            System.out.println("Balance after deposit: Rs "+ acc8.getBalance());
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        Account acc9=null;
        System.out.println(">>> Test 10: PIN Verification");
        try{
            acc9=new Account(1009,"Frank Miller",40,1500.0,"Savings");
            System.out.println("Account: "+ displayAccountLine(acc9));
            acc9.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");
            acc9.withdraw(200.0,1234);
            System.out.println("Withdrawing Rs 200.0 with correct PIN: SUCCESS");
            System.out.println("Balance: "+acc9.getBalance());
        } catch (Exception e) {
            System.out.println("EXCEPTION: " +e.getMessage());
        }
        try{
            System.out.println("Attempting to withdraw Rs 100.0 with incorrect PIN (9999)");
            acc9.withdraw(100.0,9999);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " +e.getMessage());
        }
        try{
            System.out.println("Attempting to withdraw Rs 100.0 without PIN set");
            acc1.withdraw(100.0,1234);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " +e.getMessage());
        }
        System.out.println(">>> Test 11: All Accounts Summary");
        System.out.println(displayAccountLine(acc1));
        System.out.println(displayAccountLine(acc5));
        System.out.println(displayAccountLine(acc6));
        System.out.println(displayAccountLine(acc7));
        System.out.println(displayAccountLine(acc8));
        System.out.println(displayAccountLine(acc9));
        System.out.println("============================================================");
        System.out.println("TEST COMPLETED!");
        System.out.println("============================================================");
    }
}