package net.shared.distributed.functions;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.RoutedResponse;
import net.shared.distributed.api.Capability;
import net.shared.distributed.capabilities.KryoCapabilityFunction;
import net.shared.distributed.logging.Logger;

@Capability(name = "String.toLower", nodeFunction = StringToLower.StringToLowerNodeFunction.class)
public class StringToLower {

    public String target;

    public StringToLower() {}
    public StringToLower(String target) {
        this.target = target;
    }

    public static class StringToLowerNodeFunction extends KryoCapabilityFunction<StringToLower> {
        @Override
        public void Invoke(Connection conn) {
            String s = packet.target.toLowerCase();
            conn.sendTCP(new RoutedResponse<>(new StringToLower(s)));
            Logger.instance().Debug("%s processing StringToLower request: [%s -> %s]", conn.getID(), packet.target, s);
        }
    }

}
