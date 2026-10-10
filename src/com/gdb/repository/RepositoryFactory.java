package com.gdb.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class RepositoryFactory {

    private static AccountRepository accountRepositoryInstance;
    private static TransactionRepository transactionRepositoryInstance;

    public static Properties loadPersistenceProperties() {
        Properties props = new Properties();
        String[] paths = {
            "config/persistence.properties",
            "src/main/resources/config/persistence.properties",
            "bin/config/persistence.properties"
        };

        for (String p : paths) {
            File f = new File(p);
            if (f.exists()) {
                try (InputStream in = new FileInputStream(f)) {
                    props.load(in);
                    return props;
                } catch (Exception ignored) {
                }
            }
        }

        try (InputStream in = RepositoryFactory.class.getClassLoader().getResourceAsStream("config/persistence.properties")) {
            if (in != null) {
                props.load(in);
                return props;
            }
        } catch (Exception ignored) {
        }

        return props;
    }

    public static String getPersistenceMode() {
        return loadPersistenceProperties().getProperty("persistence.mode", "memory").trim().toLowerCase();
    }

    public static synchronized AccountRepository getAccountRepository() {
        if (accountRepositoryInstance == null) {
            String mode = getPersistenceMode();
            if ("memory".equals(mode)) {
                accountRepositoryInstance = new InMemoryAccountRepository();
            } else if ("jdbc".equals(mode)) {
                throw new UnsupportedOperationException("JDBC repository not implemented yet - coming in Activity 22");
            } else if ("file".equals(mode)) {
                throw new UnsupportedOperationException("File repository not implemented yet");
            } else {
                accountRepositoryInstance = new InMemoryAccountRepository();
            }
        }
        return accountRepositoryInstance;
    }

    public static synchronized TransactionRepository getTransactionRepository() {
        if (transactionRepositoryInstance == null) {
            String mode = getPersistenceMode();
            if ("memory".equals(mode)) {
                transactionRepositoryInstance = new InMemoryTransactionRepository();
            } else if ("jdbc".equals(mode)) {
                throw new UnsupportedOperationException("JDBC repository not implemented yet - coming in Activity 22");
            } else if ("file".equals(mode)) {
                throw new UnsupportedOperationException("File repository not implemented yet");
            } else {
                transactionRepositoryInstance = new InMemoryTransactionRepository();
            }
        }
        return transactionRepositoryInstance;
    }

    public static synchronized void reset() {
        accountRepositoryInstance = null;
        transactionRepositoryInstance = null;
    }
}
