package com.gdb.tests;

import com.gdb.domain.CurrentAccount;
import com.gdb.domain.FixedDepositAccount;
import com.gdb.domain.SalaryAccount;
import com.gdb.domain.SavingsAccount;

public class TestAccountSubclasses {
    public static void main(String[] args) {
        System.out.println("=== Account Subclasses Test ===");

        SavingsAccount savingsAccount = new SavingsAccount(1001, "John Doe", 25, 10000.0);
        System.out.println("Savings Account Created: Balance Rs " + savingsAccount.getBalance() + " | Min Balance: Rs " + savingsAccount.getMinBalance());

        CurrentAccount currentAccount = new CurrentAccount(1002, "Jane Smith", 30, 10000.0);
        System.out.println("Current Account Created: Overdraft Limit Rs " + currentAccount.getOverdraftLimit());

        FixedDepositAccount fixedDepositAccount = new FixedDepositAccount(1003, "Robert Brown", 35, 10000.0, 12, 6.5);
        System.out.println("Fixed Deposit Created: Tenure " + fixedDepositAccount.getTenureMonths() + " months | Interest: " + fixedDepositAccount.getInterestRate() + "%");

        SalaryAccount salaryAccount = new SalaryAccount(1004, "Priya Shah", 28, 10000.0, "Infosys");
        System.out.println("Salary Account Created: Employer " + salaryAccount.getEmployerName());

        System.out.println("All subclasses instantiated successfully!");
    }
}
