package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TestLogHandler extends Handler {
    private final List<LogRecord> records = new ArrayList<>();

    @Override
    public void publish(LogRecord record) {
        records.add(record);
    }

    @Override
    public void flush() {
        // nothing to do
    }

    @Override
    public void close() throws SecurityException {
        records.clear();
    }

    public List<LogRecord> getRecords() {
        return records;
    }
}
