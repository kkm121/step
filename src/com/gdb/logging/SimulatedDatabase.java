package com.gdb.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulatedDatabase {

    private final Map<String, List<Object>> tables = new HashMap<>();

    public synchronized void insert(String table, Object record) {
        tables.computeIfAbsent(table, k -> new ArrayList<>()).add(record);
    }

    public synchronized List<Object> selectAll(String table) {
        List<Object> records = tables.get(table);
        if (records == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(records);
    }

    public synchronized void deleteAll(String table) {
        tables.remove(table);
    }

    public synchronized int count(String table) {
        List<Object> records = tables.get(table);
        return records != null ? records.size() : 0;
    }
}
