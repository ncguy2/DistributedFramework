package net.shared.distributed.api.internal;

import net.shared.distributed.api.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface ILogImpl {

    void Log(Logger.LogLevel level, String text);

    default String ConstructLine(Logger.LogLevel level, String text) {
        return "[" +
                ConstructDate() +
                "][" +
                level.PaddedName() +
                "] " +
                text;
    }

    default String ConstructDate() {
        SimpleDateFormat format = new SimpleDateFormat("YY/MM/dd - HH:mm:ss");
        return format.format(new Date());
    }


}
