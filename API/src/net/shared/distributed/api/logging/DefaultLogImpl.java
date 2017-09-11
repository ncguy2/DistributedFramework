package net.shared.distributed.api.logging;

import net.shared.distributed.api.internal.ILogImpl;

public class DefaultLogImpl implements ILogImpl {

    @Override
    public void Log(Logger.LogLevel level, String text) {
        if(level.ordinal() <= Logger.instance().logLevel)
            System.out.println(this.ConstructLine(level, text));
    }
}
