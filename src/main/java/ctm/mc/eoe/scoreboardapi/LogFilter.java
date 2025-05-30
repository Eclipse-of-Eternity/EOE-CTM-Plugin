package ctm.mc.eoe.scoreboardapi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;

import java.util.concurrent.TimeUnit;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class LogFilter implements Filter {
    public static boolean canceling = false;

    @Override
    public boolean isLoggable(LogRecord record) {
        return !canceling;
    }
}