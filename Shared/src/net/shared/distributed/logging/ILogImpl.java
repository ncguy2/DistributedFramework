package net.shared.distributed.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface ILogImpl {

    void Log(Logger.LogLevel level, String text);
    default String ConstructLine(Logger.LogLevel level, String text) {
        SimpleDateFormat format = new SimpleDateFormat("YY/MM/dd - HH:mm:ss");
        return "[" +
                format.format(new Date()) +
                "][" +
                level.PaddedName() +
                "]" +
                text;
    }


}
