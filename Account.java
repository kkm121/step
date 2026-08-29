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

    public Account(int accountNumber, String name, int age,double initialBalance, String accountType) throws IllegalArgumentException {
        this.accountNumber = accountNumber;
        this.name =name;
        if(age<MIN_AGE){
            throw new IllegalArgumentException("Customer must be at least 18 years old. Provided: " + age);
        }
        else{
            this.age=age;
        }
        this.accountType =accType(accountType);
        this.status="Active";
        this.pin=null;
        if(initialBalance<minbalance(this.accountType)){
            throw new IllegalArgumentException(this.accountType + " account requires minimum balance of Rs " + minbalance(this.accountType) + ". Provided: Rs " + initialBalance);
        }
        else{
            this.balance=initialBalance;
        }
    }
    private String accType(String accountType) {
        if ("Current".equalsIgnoreCase(accountType)) {
            return "Current";
        }
        else if ("Savings".equalsIgnoreCase(accountType)){
            return "Savings";
        }
        else{
            throw new IllegalArgumentException("Account type must be 'Savings' or 'Current'. Provided: " + accountType);
        }
    }
    private double minbalance(String accountType) {
        if ("Current".equalsIgnoreCase(accountType)) {
            return MIN_CURRENT;
        }
        return MIN_SAVINGS;
    }
    private void validateActive() throws InactiveAccountException {
        if(!"Active".equalsIgnoreCase(status)){
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
    }
    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        validateActive();
        if(amount<=0){
            throw new InvalidAmountException("Deposit amount must be positive. Provided: Rs " + amount);
        }
        this.balance+=amount;
    }
    public void withdraw(double amount, int pin) throws InactiveAccountException,InvalidAmountException, InvalidPinException, MinimumBalanceViolationException, InsufficientBalanceException {
        validateActive();
        if (this.pin==null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        if (!verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        if (amount<= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: Rs " + amount);
        }
        if(this.balance<amount){
            throw new InsufficientBalanceException("Insufficient balance. Available: Rs " + this.balance + ", Requested: Rs " + amount);
        }
        if((this.balance-amount)<minbalance(this.accountType)){
            throw new MinimumBalanceViolationException("Cannot withdraw. Minimum balance of Rs " + minbalance(this.accountType) + " required. Available after withdrawal: Rs " + (this.balance-amount));
        }
        this.balance-=amount;
    }

    public void withdraw(double amount) throws InactiveAccountException,InvalidAmountException, InvalidPinException, MinimumBalanceViolationException, InsufficientBalanceException {
        validateActive();
        if (amount<= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: Rs " + amount);
        }
        if(this.balance<amount){
            throw new InsufficientBalanceException("Insufficient balance. Available: Rs " + this.balance + ", Requested: Rs " + amount);
        }
        if((this.balance-amount)<minbalance(this.accountType)){
            throw new MinimumBalanceViolationException("Cannot withdraw. Minimum balance of Rs " + minbalance(this.accountType) + " required. Available after withdrawal: Rs " + (this.balance-amount));
        }
        this.balance-=amount;
    }

    public void closeAccount() throws IllegalStateException {
        if ("Inactive".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Account is already inactive");
        }
        status ="Inactive";
    }

    public void reopenAccount() throws IllegalStateException {
        if ("Active".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Account is already active");
        }
        status ="Active";
    }

    public void setPin(int pin) throws IllegalArgumentException {
        if (pin<MIN_PIN||pin>MAX_PIN) {
            throw new IllegalArgumentException("Invalid PIN");
        }
        this.pin=pin;
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
        this.age =age;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) throws IllegalArgumentException {
        String accType1 =accType(accountType);
        if (this.balance<minbalance(accType1)) {
            throw new IllegalArgumentException("Balance below minimum for the new account type");
        }
        this.accountType=accType1;
    }
    public String getStatus() {
        return status;
    }

    public Integer getPin() {
        return pin;
    }
}