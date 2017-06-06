package net.shared.distributed.capabilities;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.Registry;
import net.shared.distributed.event.host.CapabilityResponseEvent;
import net.shared.distributed.logging.Logger;

import java.io.Serializable;
import java.util.Set;

public class CapabilityPacket {

    @Capability(name = "Capability.request", nodeFunction = Request.RequestFunction.class)
    public static class Request implements Serializable {

        public static final long serialVersionUID = Registry.SerialVersions.CAPABILITY_REQUEST;

        public static class RequestFunction extends CapabilityFunction<Request> {
            @Override
            public void Invoke(Connection conn) {
                Set<String> strings = Capabilities.instance().GetNodeCapabilities();
                String[] caps = new String[strings.size()];
                strings.toArray(caps);
                Response resp = new Response(caps);
                conn.sendTCP(resp);
            }
        }

    }

    @Capability(name = "Capability.response", hostFunction = Response.ResponseFunction.class)
    public static class Response implements Serializable {
        public static final long serialVersionUID = Registry.SerialVersions.CAPABILITY_RESPONSE;

        public String[] capabilities;

        public Response() {
            capabilities = new String[0];
        }

        public Response(String... capabilities) {
            this.capabilities = capabilities;
        }

        public static class ResponseFunction extends CapabilityFunction<Response> {

            @Override
            public void Invoke(Connection conn) {
                new CapabilityResponseEvent(conn.getID(), packet).Fire();
                Logger.instance().Info("Node capabilities received: ");
                for (String cap : packet.capabilities)
                    Logger.instance().Debug("\t" + cap);
            }
        }

    }

}
