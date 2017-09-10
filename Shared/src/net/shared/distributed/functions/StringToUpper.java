package net.shared.distributed.functions;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.RoutedResponse;
import net.shared.distributed.api.Capability;
import net.shared.distributed.capabilities.KryoCapabilityFunction;
import net.shared.distributed.logging.Logger;

@Capability(name = "String.toUpper", nodeFunction = StringToUpper.StringToUpperNodeFunction.class, hostFunction = StringToUpper.StringToUpperHostFunction.class)
public class StringToUpper {

    public String target;

    public StringToUpper() {}

    public StringToUpper(String target) {
        this.target = target;
    }

    public static class StringToUpperNodeFunction extends KryoCapabilityFunction<StringToUpper> {

        @Override
        public void Invoke(Connection conn) {
            String s = packet.target.toUpperCase();
            conn.sendTCP(new RoutedResponse<>(new StringToUpper(s)));
            Logger.instance().Debug(String.format("%s processing StringToUpper request: [%s -> %s]", conn.getID(), packet.target, s));
        }
    }

    public static class StringToUpperHostFunction extends KryoCapabilityFunction<StringToUpper> {

        @Override
        public void Invoke(Connection conn) {

        }
    }

}
