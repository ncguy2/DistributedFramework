package net.shared.distributed.core;

import net.shared.distributed.capabilities.Capabilities;
import net.shared.distributed.api.Capability;
import net.shared.distributed.distributor.Distributor;

/**
 * Created by Guy on 16/05/2017.
 */
public class Core {

    public Distributor distributor;

    public Core() {
        Capabilities.instance().SetSide(Capability.Side.HOST);
    }

    public void SetDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

}
