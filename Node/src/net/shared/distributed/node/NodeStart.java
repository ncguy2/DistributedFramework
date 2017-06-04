package net.shared.distributed.node;

import net.shared.distributed.Registry;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.network.AsyncNetworkSocket;
import net.shared.distributed.network.commands.NodeShutdownCommand;
import net.shared.distributed.node.logging.NodeLogImpl;
import net.shared.distributed.node.operation.NodeOperator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class NodeStart {

    static String coreHost = "";
    static String arg = "";
    static String[] args = null;
    static int index;
    static int corePort = Registry.NODE_COMM_PORT;

    private static boolean alive = true;
    private static Queue<NodeOperator> operators;
    private static long updateFreq = 50;

    static boolean hasHost = false;

    public static void main(String[] args) {
        NodeStart.args = args;
        for (index = 0; index < args.length; index++) {
            arg = args[index];
            if (index < args.length - 1 && GetCoreHost("-host", "-h"))
                continue;
            if (index < args.length - 1 && GetCorePort("-port", "-p"))
                continue;
        }

        if (!hasHost) {
            System.err.println("No host address provided, provide a host address using the '-host' switch");
            return;
        }

        InetAddress addr;
        try {
            addr = InetAddress.getByName(coreHost);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println(coreHost + " is not a valid address");
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                AsyncNetworkSocket socket = new AsyncNetworkSocket(coreHost, corePort);
                socket.Send(new NodeShutdownCommand());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        operators = new LinkedList<>();

        Logger.instance().SetLogImpl(new NodeLogImpl(addr.getHostAddress()));

        NodeCore node = new NodeCore(addr, corePort);
        try {
            node.Initialize().Register();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (alive) {
            NodeOperator poll = operators.poll();
            if (poll != null)
                poll.Invoke();
            try {
                Thread.sleep(updateFreq);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean GetCoreHost(String... switches) {
        for (String switche : switches) {
            if (arg.equalsIgnoreCase(switche)) {
                coreHost = args[index + 1];
                hasHost = true;
                return true;
            }
        }
        return false;
    }

    private static boolean GetCorePort(String... switches) {
        for (String switche : switches) {
            if (arg.equalsIgnoreCase(switche)) {
                corePort = Integer.parseInt(args[index+1]);
                return true;
            }
        }
        return false;
    }

    public static class NodeInterface {

        public static void PostOperator(NodeOperator operator) {
            NodeStart.operators.add(operator);
        }

        public static void Kill() {
            NodeStart.alive = false;
        }

    }

}
