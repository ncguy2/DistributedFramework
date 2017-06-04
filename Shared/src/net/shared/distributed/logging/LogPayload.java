package net.shared.distributed.logging;

import net.shared.distributed.Registry;

import java.io.Serializable;

public class LogPayload implements Serializable {

    public static long serialVersionUID = Registry.SerialVersions.LOG_PAYLOAD;

    public Logger.LogLevel level;
    public String text;

    public LogPayload() {
    }

    public LogPayload(Logger.LogLevel level, String text) {
        this.level = level;
        this.text = text;
    }
}
