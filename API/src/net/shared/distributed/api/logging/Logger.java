package net.shared.distributed.api.logging;

import net.shared.distributed.api.internal.ILogImpl;

import static net.shared.distributed.api.logging.Logger.LogLevel.*;

public class Logger {

    private static Logger instance;
    public static Logger instance() {
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    public int logLevel = VERBOSE.ordinal();

    private Logger() {
        SetLogImpl(new DefaultLogImpl());
    }

    private ILogImpl impl;

    public void SetLogImpl(ILogImpl impl) {
        this.impl = impl;
    }

    public void Log(LogLevel level, String text) {
        this.impl.Log(level, text);
    }

    public void Info   (String text) { Log(INFO,    text); }
    public void Warn   (String text) { Log(WARN,    text); }
    public void Error  (String text) { Log(ERROR,   text); }
    public void Fatal  (String text) { Log(FATAL,   text); }
    public void Debug  (String text) { Log(DEBUG,   text); }
    public void Verbose(String text) { Log(VERBOSE, text); }

    public void Info   (String format, Object... args) { Info   (String.format(format, args)); }
    public void Warn   (String format, Object... args) { Warn   (String.format(format, args)); }
    public void Error  (String format, Object... args) { Error  (String.format(format, args)); }
    public void Fatal  (String format, Object... args) { Fatal  (String.format(format, args)); }
    public void Debug  (String format, Object... args) { Debug  (String.format(format, args)); }
    public void Verbose(String format, Object... args) { Verbose(String.format(format, args)); }


    public enum LogLevel {
        FATAL,
        ERROR,
        WARN,
        INFO,
        DEBUG,
        VERBOSE
        ;

        public static int longest = 7;

        public String PaddedName() {
            return String.format("%1$"+longest+"s", name());
        }

    }

}
