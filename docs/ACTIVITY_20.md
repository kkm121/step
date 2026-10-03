# Activity 20: AccountUI (Interactive Console Application)

## Overview
Activity 20 introduces `AccountUI`, a menu-driven interactive console user interface built on top of `AccountService`. It provides users and administrators with an accessible terminal portal to perform core banking operations, manage accounts, and view transaction audit histories.

## Architecture

```
┌──────────────────────────────────────────────┐
│                  Main                        │  Application Launcher
└──────────────────────┬───────────────────────┘
                       │ launches
                       ▼
┌──────────────────────────────────────────────┐
│                AccountUI                     │  Presentation Layer (Console Menu)
└──────────────────────┬───────────────────────┘
                       │ delegates to
                       ▼
┌──────────────────────────────────────────────┐
│              AccountService                  │  Service Layer (Orchestrator)
└───┬──────────────────┬───────────────────┬───┘
    │                  │                   │
    ▼                  ▼                   ▼
┌──────────────┐ ┌──────────────┐ ┌─────────────────────────┐
│AccountFactory│ │TransferService││    TransactionLogger    │
└──────────────┘ └──────────────┘ └────────────┬────────────┘
                                               │
                                               ▼ Bridge
                                  ┌─────────────────────────┐
                                  │     LogDestination      │
                                  ├────────────┬────────────┤
                                  │ FileLog    │ MemoryLog  │
                                  │ DatabaseLog│            │
                                  └────────────┴────────────┘
```

## Menu Options

1. **Open Account**: Interactively prompts for account type (`SAVINGS`, `CURRENT`, `SALARY`, `FIXED_DEPOSIT`), customer name, age, initial deposit, PIN, and tenure years.
2. **View Account Details**: Retrieves and prints full account info for a given account number.
3. **List All Accounts**: Displays a tabular list of all currently active accounts in the registry.
4. **Deposit Funds**: Prompts for account number and deposit amount; executes a `DepositCommand`.
5. **Withdraw Funds**: Prompts for account number, withdrawal amount, and PIN; validates funds and daily limits via `WithdrawCommand`.
6. **Transfer Funds**: Prompts for source and destination accounts, amount, and source PIN; validates daily limits and atomically moves funds via `TransferCommand`.
7. **View Transaction History**: Displays the serialized audit trail of all executed commands.
8. **Close Account**: Prompts for account number and PIN to deactivate an account.
0. **Exit**: Gracefully exits the application.

## How to Compile & Run

### Compile
```powershell
New-Item bin -ItemType Directory -Force | Out-Null
Copy-Item src\main\resources\config bin -Recurse -Force
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
```

### Run Test Driver
```powershell
java -cp "bin;src/main/resources" com.gdb.tests.TestAccountUI
```

### Run Interactive Console App
```powershell
java -cp "bin;src/main/resources" com.gdb.Main
```
