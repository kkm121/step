package com.gdb.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class AccountRulesPropertiesLoader {

    private static final Properties properties = new Properties();
    private static final Map<String, Properties> accountTypeProperties = new HashMap<>();
    private static final Set<String> loadedFiles = new HashSet<>();

    private AccountRulesPropertiesLoader() {
    }

    public static Properties loadProperties(String filePath) {
        return loadProperties(filePath, true);
    }

    public static Properties loadProperties(String filePath, boolean verbose) {
        Properties fileProps = new Properties();
        File file = new File(filePath);
        boolean loaded = false;

        if (file.exists()) {
            try (InputStream input = new FileInputStream(file)) {
                fileProps.load(input);
                loaded = true;
            } catch (IOException e) {
                System.err.println("[Config Error] Failed to read from file: " + filePath);
            }
        }

        if (!loaded) {
            String resourcePath = filePath.replace('\\', '/');
            if (resourcePath.startsWith("src/main/resources/")) {
                resourcePath = resourcePath.substring("src/main/resources/".length());
            }

            try (InputStream cpInput = AccountRulesPropertiesLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (cpInput != null) {
                    fileProps.load(cpInput);
                    loaded = true;
                } else {
                    System.err.println("[Config Error] Properties file not found: " + filePath);
                }
            } catch (IOException e) {
                System.err.println("[Config Error] Failed to read resource stream: " + resourcePath);
            }
        }

        if (loaded) {
            properties.putAll(fileProps);
            String normPath = filePath.replace('\\', '/').toLowerCase();
            String accountType = null;
            if (normPath.contains("savings")) accountType = "SAVINGS";
            else if (normPath.contains("current")) accountType = "CURRENT";
            else if (normPath.contains("fixeddeposit") || normPath.contains("fixed_deposit")) accountType = "FIXEDDEPOSIT";
            else if (normPath.contains("salary")) accountType = "SALARY";

            if (accountType != null) {
                accountTypeProperties.put(accountType, fileProps);
            }

            if (!loadedFiles.contains(filePath)) {
                loadedFiles.add(filePath);
                if (verbose) {
                    System.out.println("[Config] Loaded rules from " + filePath);
                }
            }
        }

        return fileProps;
    }

    public static Properties getPropertiesForAccountType(String accountType) {
        if (accountType == null) return null;
        String key = accountType.toUpperCase().replace("_", "");
        return accountTypeProperties.get(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static double getDouble(String key) {
        return getDouble(key, 0.0);
    }
}
