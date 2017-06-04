package net.shared.distributed.logging;

import static net.shared.distributed.logging.Logger.LogLevel.*;

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
