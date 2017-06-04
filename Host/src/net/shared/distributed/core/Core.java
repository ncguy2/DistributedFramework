package net.shared.distributed.core;

import net.shared.distributed.TypeFunctionHandler;
import net.shared.distributed.distributor.Distributor;

/**
 * Created by Guy on 16/05/2017.
 */
public class Core {

    public TypeFunctionHandler handler;
    public Distributor distributor;

    public Core() {
        handler = new TypeFunctionHandler();
    }

    public void SetDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public TypeFunctionHandler GetHandler() {
        return handler;
    }
}
