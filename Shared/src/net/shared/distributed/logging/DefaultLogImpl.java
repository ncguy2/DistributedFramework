package net.shared.distributed.logging;

public class DefaultLogImpl implements ILogImpl {

    @Override
    public void Log(Logger.LogLevel level, String text) {
        if(level.ordinal() <= Logger.instance().logLevel)
            System.out.println(this.ConstructLine(level, text));
    }
}
