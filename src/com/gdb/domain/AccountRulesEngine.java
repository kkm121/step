package com.gdb.domain;

import java.time.LocalDateTime;
import java.util.Properties;

public final class AccountRulesEngine {

    private static final AccountRulesEngine INSTANCE = new AccountRulesEngine();
    private static boolean rulesLoaded = false;

    private AccountRulesEngine() {
    }

    public static AccountRulesEngine getInstance() {
        ensureAllRulesLoaded();
        return INSTANCE;
    }

    public static void loadRules() {
        System.out.println("📂 Loading account rules from properties files...");
        System.out.println("--------------------------------------------------");
        AccountRulesPropertiesLoader.loadProperties("src/main/resources/config/rules/savings.properties", false);
        System.out.println("✅ Loaded rules for: SAVINGS (4 tenure buckets)");
        AccountRulesPropertiesLoader.loadProperties("src/main/resources/config/rules/current.properties", false);
        System.out.println("✅ Loaded rules for: CURRENT (3 tenure buckets)");
        AccountRulesPropertiesLoader.loadProperties("src/main/resources/config/rules/fixeddeposit.properties", false);
        System.out.println("✅ Loaded rules for: FIXEDDEPOSIT (4 tenure buckets)");
        AccountRulesPropertiesLoader.loadProperties("src/main/resources/config/rules/salary.properties", false);
        System.out.println("✅ Loaded rules for: SALARY (4 tenure buckets)");
        System.out.println("--------------------------------------------------");
        System.out.println("✅ All rules loaded successfully!");
        System.out.println("   Loaded at: " + LocalDateTime.now());
        System.out.println("   Account types: [SALARY, SAVINGS, FIXEDDEPOSIT, CURRENT]\n");
        rulesLoaded = true;
    }

    public static void ensureAllRulesLoaded() {
        if (!rulesLoaded) {
            AccountRulesPropertiesLoader.loadProperties("src/main/resources/config/rules/savings.properties", false);
            AccountRulesPropertiesLoader.loadProperties("src/main/resources/config/rules/current.properties", false);
            AccountRulesPropertiesLoader.loadProperties("src/main/resources/config/rules/fixeddeposit.properties", false);
            AccountRulesPropertiesLoader.loadProperties("src/main/resources/config/rules/salary.properties", false);
            rulesLoaded = true;
        }
    }

    private static String getTenureBucket(int tenureYears) {
        int tenure = Math.max(0, tenureYears);
        if (tenure >= 5) {
            return "privilege";
        } else if (tenure >= 3) {
            return "premium";
        } else if (tenure >= 1) {
            return "standard";
        } else {
            return "new";
        }
    }

    public static double getSavingsMinBalance(int tenureYears) {
        ensureAllRulesLoaded();
        String bucket = getTenureBucket(tenureYears);
        return AccountRulesPropertiesLoader.getDouble("savings.min.balance." + bucket, 10000.0);
    }

    public static double getSavingsInterestRate(int tenureYears) {
        ensureAllRulesLoaded();
        String bucket = getTenureBucket(tenureYears);
        return AccountRulesPropertiesLoader.getDouble("savings.interest.rate." + bucket, 2.70);
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        ensureAllRulesLoaded();
        double minLimit = AccountRulesPropertiesLoader.getDouble("current.overdraft.min.limit", 25000.0);
        double multiplier = AccountRulesPropertiesLoader.getDouble("current.overdraft.multiplier", 2.5);
        return Math.max(minLimit, monthlyTurnover * multiplier);
    }

    public static double getFDInterestRate(int months) {
        ensureAllRulesLoaded();
        double longMin = AccountRulesPropertiesLoader.getDouble("fixeddeposit.tier.long.min.months", 12.0);
        double mediumMin = AccountRulesPropertiesLoader.getDouble("fixeddeposit.tier.medium.min.months", 6.0);
        if (months >= (int) longMin) {
            return AccountRulesPropertiesLoader.getDouble("fixeddeposit.interest.rate.long", 6.5);
        } else if (months >= (int) mediumMin) {
            return AccountRulesPropertiesLoader.getDouble("fixeddeposit.interest.rate.medium", 6.0);
        } else {
            return AccountRulesPropertiesLoader.getDouble("fixeddeposit.interest.rate.short", 5.5);
        }
    }

    public Object getAdditionalFeature(String accountType, int tenureYears, String featureName) {
        ensureAllRulesLoaded();
        if (accountType == null || featureName == null) {
            return null;
        }

        String bucket = getTenureBucket(tenureYears);
        Properties props = AccountRulesPropertiesLoader.getPropertiesForAccountType(accountType);

        if ("dailyTransferLimit".equalsIgnoreCase(featureName)) {
            String propKey = "daily.transfer.limit." + bucket;
            if (props != null && props.containsKey(propKey)) {
                try {
                    return Double.parseDouble(props.getProperty(propKey).trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        }

        return null;
    }

    public double getDailyTransferLimit(String accountType, int tenureYears) {
        Object feature = getAdditionalFeature(accountType, tenureYears, "dailyTransferLimit");
        return feature == null ? 0.0 : (Double) feature;
    }
}
