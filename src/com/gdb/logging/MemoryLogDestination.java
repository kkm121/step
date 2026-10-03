package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.ArrayList;
import java.util.List;

public class MemoryLogDestination implements LogDestination {

    private final List<TransactionCommand> storage = new ArrayList<>();

    @Override
    public synchronized void write(TransactionCommand cmd) {
        if (cmd != null) {
            storage.add(cmd);
        }
    }

    @Override
    public synchronized List<TransactionCommand> readAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public synchronized void clear() {
        storage.clear();
    }

    @Override
    public String getDestinationName() {
        return "MEMORY";
    }
}
