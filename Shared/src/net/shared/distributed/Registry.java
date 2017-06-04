package net.shared.distributed;

public class Registry {

    public static final int NODE_COMM_PORT = 3301;
    public static final int REMOTE_LOG_PORT = 3302;

    public static class SerialVersions {
        public static final long LOG_PAYLOAD = 0xFFFF_0001L;
        public static final long KILL_COMMAND = 0xFFFF_0002L;
        public static final long SHUTDOWN_COMMAND = 0xFFFF_0003L;
    }

}
