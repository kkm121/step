package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TransactionLog {

    private static final String DATA_DIR = "data";
    private static final String LOG_FILE = "data/transactions.ser";

    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }

    public synchronized void log(TransactionCommand cmd) {
        if (cmd == null) return;

        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(LOG_FILE);
        boolean append = file.exists() && file.length() > 0;

        try {
            OutputStream fileOut = new FileOutputStream(file, true);
            ObjectOutputStream objOut = append
                    ? new AppendableObjectOutputStream(fileOut)
                    : new ObjectOutputStream(fileOut);

            objOut.writeObject(cmd);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            System.err.println("[Log Error] Failed to write command to log: " + e.getMessage());
        }
    }

    public synchronized List<TransactionCommand> readAll() {
        List<TransactionCommand> commands = new ArrayList<>();
        File file = new File(LOG_FILE);
        if (!file.exists() || file.length() == 0) {
            return commands;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    TransactionCommand cmd = (TransactionCommand) in.readObject();
                    commands.add(cmd);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("[Log Error] Failed to read commands from log: " + e.getMessage());
        }

        return commands;
    }

    public synchronized void clear() {
        File file = new File(LOG_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
