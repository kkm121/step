package com.gdb.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {

    public static void initialize(ConnectionProvider provider) {
        if (provider==null) {
            return;
        }
        String sql=readSchemaSql();
        if (sql==null||sql.trim().isEmpty()) {
            return;
        }
        try (Connection conn=provider.getConnection();
             Statement stmt=conn.createStatement()) {
            String[] statements=sql.split(";");
            for (String s:statements) {
                String trimmed=s.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database schema: " + e.getMessage(),e);
        }
    }

    public static String readSchemaSql() {
        String[] paths={
            "schema.sql",
            "src/main/resources/schema.sql",
            "bin/schema.sql"
        };
        for (String p:paths) {
            File f=new File(p);
            if (f.exists()) {
                try (InputStream in=new FileInputStream(f)) {
                    return readFromStream(in);
                } catch (Exception ignored) {
                }
            }
        }
        try (InputStream in=SchemaInitializer.class.getClassLoader().getResourceAsStream("schema.sql")) {
            if (in!=null) {
                return readFromStream(in);
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    private static String readFromStream(InputStream in) throws Exception {
        StringBuilder sb=new StringBuilder();
        try (BufferedReader reader=new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8))) {
            String line;
            while ((line=reader.readLine())!=null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
