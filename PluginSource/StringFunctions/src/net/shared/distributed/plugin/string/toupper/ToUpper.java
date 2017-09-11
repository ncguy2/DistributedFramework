package net.shared.distributed.plugin.string.toupper;

import net.shared.distributed.api.Capability;
import net.shared.distributed.api.CapabilityFunction;
import net.shared.distributed.api.internal.IConnection;
import net.shared.distributed.api.logging.Logger;

@Capability(name = "String::ToUpper", nodeFunction = ToUpper.ToUpperNodeFunction.class)
public class ToUpper {

    public String target;

    public ToUpper() {
        this("");
    }

    public ToUpper(String target) {
        this.target = target;
    }

    public static class ToUpperNodeFunction extends CapabilityFunction<ToUpper, IConnection> {

        @Override
        public void Invoke(IConnection conn) {
            String s = packet.target.toUpperCase();
            conn.SendRouted(s);
            Logger.instance().Debug(String.format("%s processing ToUpper request: [%s -> %s]", conn.GetId(), packet.target, s));
        }
    }



}
