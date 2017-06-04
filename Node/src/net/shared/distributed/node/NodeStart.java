package net.shared.distributed.node;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.FrameworkMessage;
import net.shared.distributed.Registry;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.node.logging.NodeLogImpl;
import net.shared.distributed.node.operation.NodeOperator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import static net.shared.distributed.Registry.RegisterKryoClasses;

public class NodeStart {

    static String coreHost = "";
    static String arg = "";
    static String[] args = null;
    static int index;
    static int coreTCP = Registry.TCP_PORT;
    static int coreUDP = Registry.UDP_PORT;

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
            if (index < args.length - 1 && GetCoreTCP("-tcp"))
                continue;
            if (index < args.length - 1 && GetCoreUDP("-udp"))
                continue;
        }

        if (!hasHost) {
            System.err.println("No host address provided, provide a host address using the '-host' switch");
            return;
        }

        operators = new LinkedList<>();

        Client client = new Client();
        RegisterKryoClasses(client.getKryo());
        client.setKeepAliveTCP(1000);
        Thread clientThread = new Thread(() -> {
            while(!client.isConnected()) {
                try {
                    client.update(2500);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            while(client.isConnected()) {
                client.sendTCP(new FrameworkMessage.KeepAlive());
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();

        try {
            client.connect(10000, coreHost, coreTCP);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Logger.instance().SetLogImpl(new NodeLogImpl(client));
        NodeCore node = new NodeCore(client);
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

    private static boolean GetCoreTCP(String... switches) {
        for (String switche : switches) {
            if (arg.equalsIgnoreCase(switche)) {
                coreTCP = Integer.parseInt(args[index+1]);
                return true;
            }
        }
        return false;
    }

    private static boolean GetCoreUDP(String... switches) {
        for (String switche : switches) {
            if (arg.equalsIgnoreCase(switche)) {
                coreUDP = Integer.parseInt(args[index+1]);
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
