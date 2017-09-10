package net.shared.distributed.capabilities;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.Registry;
import net.shared.distributed.api.Capability;
import net.shared.distributed.event.host.CapabilityResponseEvent;
import net.shared.distributed.event.node.NameResponseEvent;
import net.shared.distributed.logging.Logger;

import java.io.Serializable;
import java.util.Set;

public class CapabilityPacket {

    @Capability(name = "Capability.request", nodeFunction = Request.RequestFunction.class, internal = true)
    public static class Request implements Serializable {

        public static final long serialVersionUID = Registry.SerialVersions.CAPABILITY_REQUEST;

        public static class RequestFunction extends KryoCapabilityFunction<Request> {
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

    @Capability(name = "Capability.response", hostFunction = Response.ResponseFunction.class, internal = true)
    public static class Response implements Serializable {
        public static final long serialVersionUID = Registry.SerialVersions.CAPABILITY_RESPONSE;

        public String[] capabilities;

        public Response() {
            capabilities = new String[0];
        }

        public Response(String... capabilities) {
            this.capabilities = capabilities;
        }

        public static class ResponseFunction extends KryoCapabilityFunction<Response> {

            @Override
            public void Invoke(Connection conn) {
                new CapabilityResponseEvent(conn.getID(), packet).Fire();
                Logger.instance().Info("Node capabilities received: ");
                for (String cap : packet.capabilities)
                    Logger.instance().Debug("\t" + cap);
            }
        }
    }

    @Capability(name = "Name.request", nodeFunction = NameRequest.NameRequestFunction.class, internal = true)
    public static class NameRequest implements Serializable {
        public static final long serialVersionUID = Registry.SerialVersions.NAME_REQUEST;

        public static class NameRequestFunction extends KryoCapabilityFunction<NameRequest> {

            @Override
            public void Invoke(Connection conn) {
                NameResponse resp = new NameResponse(Registry.name);
                conn.sendTCP(resp);
            }
        }
    }

    @Capability(name = "Name.response", hostFunction = NameResponse.NameResponseFunction.class, internal = true)
    public static class NameResponse implements Serializable  {
        public static final long serialVersionUId = Registry.SerialVersions.NAME_RESPONSE;

        public String name;

        public NameResponse() {
            this("Undefined");
        }

        public NameResponse(String name) {
            this.name = name;
        }

        public static class NameResponseFunction extends KryoCapabilityFunction<NameResponse> {
            @Override
            public void Invoke(Connection conn) {
                new NameResponseEvent(conn.getID(), packet.name).Fire();
                Logger.instance().Info("Node name received: " + packet.name);
            }
        }

    }

}
