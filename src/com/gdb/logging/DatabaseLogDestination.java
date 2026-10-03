package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLogDestination implements LogDestination {

    private static final String TABLE_NAME = "transaction_log";
    private final SimulatedDatabase db;

    public DatabaseLogDestination(SimulatedDatabase db) {
        this.db = db;
    }

    @Override
    public void write(TransactionCommand cmd) {
        db.insert(TABLE_NAME, cmd);
    }

    @Override
    public List<TransactionCommand> readAll() {
        List<Object> rows = db.selectAll(TABLE_NAME);
        List<TransactionCommand> commands = new ArrayList<>();
        for (Object row : rows) {
            if (row instanceof TransactionCommand cmd) {
                commands.add(cmd);
            }
        }
        return commands;
    }

    @Override
    public void clear() {
        db.deleteAll(TABLE_NAME);
    }

    @Override
    public String getDestinationName() {
        return "DATABASE";
    }
}
