package takenoko.utils;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
        return formatMessage(logRecord) + System.lineSeparator();
    }
}
