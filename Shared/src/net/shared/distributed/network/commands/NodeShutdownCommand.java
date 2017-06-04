package net.shared.distributed.network.commands;

import net.shared.distributed.Registry;

import java.io.Serializable;

public class NodeShutdownCommand implements Serializable {

    public static final long serialVersionUID = Registry.SerialVersions.SHUTDOWN_COMMAND;

}
