package net.shared.distributed.logging;

public class DefaultLogImpl implements ILogImpl {

    @Override
    public void Log(Logger.LogLevel level, String text) {
        System.out.println(this.ConstructLine(level, text));
    }
}
