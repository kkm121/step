package com.gdb.repository;

import com.gdb.domain.IAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<Integer, IAccount> accounts = new HashMap<>();
    private int nextAccountNumber = 1001;

    private int extractId(IAccount account) {
        if (account == null) {
            return 0;
        }
        try {
            return Integer.parseInt(account.getAccountNumber().replaceAll("\\D", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void save(IAccount account) {
        if (account != null) {
            accounts.put(extractId(account), account);
        }
    }

    @Override
    public IAccount findById(int accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public List<IAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void update(IAccount account) {
        if (account != null) {
            int id = extractId(account);
            if (accounts.containsKey(id)) {
                accounts.put(id, account);
            }
        }
    }

    @Override
    public void delete(int accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public boolean exists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    @Override
    public synchronized int nextAccountNumber() {
        return nextAccountNumber++;
    }
}
