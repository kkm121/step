public class Account {
    //Constants
    private static final int MIN_AGE=18;
    private static final double MIN_SAVINGS=500.0;
    private static final double MIN_CURRENT=1000.0;
    private static final int MIN_PIN=1000;
    private static final int MAX_PIN=9999;
    //Fields
    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    public Account(int accountNumber, String name, int age,double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age < MIN_AGE ? MIN_AGE : age;
        this.accountType = accType(accountType);
        this.status = "Active";
        this.pin = null;
        this.balance = Math.max(initialBalance, minbalance(this.accountType));
    }
    private String accType(String accountType) {
        if ("Current".equalsIgnoreCase(accountType)) {
            return "Current";
        }
        return "Savings";
    }

    private double minbalance(String accountType) {
        if ("Current".equalsIgnoreCase(accountType)) {
            return MIN_CURRENT;
        }
        return MIN_SAVINGS;
    }

    public boolean deposit(double amount) {
        if (!"Active".equalsIgnoreCase(status) || amount<=0) {
            return false;
        }
        balance+=amount;
        return true;
    }
    public boolean withdraw(double amount, int pin) {
        if (!"Active".equalsIgnoreCase(status) || amount<= 0 ||!verifyPin(pin)) {
            return false;
        }
        double minimumBalance =minbalance(accountType);
        if ((balance - amount) <minimumBalance) {
            return false;
        }

        balance-=amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (!"Active".equalsIgnoreCase(status) || amount<= 0) {
            return false;
        }

        double minimumBalance = minbalance(accountType);
        if ((balance-amount) <minimumBalance) {
            return false;
        }

        balance -=amount;
        return true;
    }

    public boolean closeAccount() {
        if ("Inactive".equalsIgnoreCase(status)) {
            return false;
        }

        status ="Inactive";
        return true;
    }

    public boolean reopenAccount() {
        if ("Active".equalsIgnoreCase(status)) {
            return false;
        }

        status ="Active";
        return true;
    }

    public boolean setPin(int pin) {
        if (pin < MIN_PIN|| pin > MAX_PIN) {
            return false;
        }
        this.pin =pin;
        return true;
    }

    public boolean verifyPin(int pin) {
        return this.pin != null && this.pin== pin;
    }

    public boolean hasPin() {
        return pin != null;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age < MIN_AGE ? MIN_AGE : age;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType =accType(accountType);
        if (balance <minbalance(this.accountType)) {
            balance =minbalance(this.accountType);
        }
    }
    public String getStatus() {
        return status;
    }

    public Integer getPin() {
        return pin;
    }
}