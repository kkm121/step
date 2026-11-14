package com.gdb.db;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class JdbcConnectionProvider implements ConnectionProvider {

    private final String url;
    private final String driver;

    public JdbcConnectionProvider(String url,String driver) {
        this.url=url;
        this.driver=driver;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            boolean loaded=false;
            File libDir=new File("lib");
            if (libDir.exists()&&libDir.isDirectory()) {
                File[] jars=libDir.listFiles((dir,name)->name.endsWith(".jar"));
                if (jars!=null&&jars.length>0) {
                    try {
                        URL[] urls=new URL[jars.length];
                        for (int i=0;i<jars.length;i++) {
                            urls[i]=jars[i].toURI().toURL();
                        }
                        URLClassLoader loader=new URLClassLoader(urls,JdbcConnectionProvider.class.getClassLoader());
                        Class<?> driverClass=Class.forName(driver,true,loader);
                        Driver d=(Driver) driverClass.getDeclaredConstructor().newInstance();
                        DriverManager.registerDriver(new DriverShim(d));
                        loaded=true;
                    } catch (Exception ignored) {
                    }
                }
            }
            if (!loaded) {
                throw new RuntimeException("Failed to load JDBC driver: " + driver,e);
            }
        }
    }

    public JdbcConnectionProvider(String url) {
        this(url,"org.sqlite.JDBC");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public String getProviderName() {
        return "JdbcConnectionProvider [" + url + "]";
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    private static class DriverShim implements Driver {
        private final Driver driver;
        DriverShim(Driver driver) {
            this.driver=driver;
        }
        @Override
        public Connection connect(String url,Properties info) throws SQLException {
            return driver.connect(url,info);
        }
        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }
        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url,Properties info) throws SQLException {
            return driver.getPropertyInfo(url,info);
        }
        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }
        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }
        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }
        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return driver.getParentLogger();
        }
    }
}
