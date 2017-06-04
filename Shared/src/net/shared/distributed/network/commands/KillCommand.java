package net.shared.distributed.network.commands;

import net.shared.distributed.Registry;

import java.io.Serializable;

public class KillCommand implements Serializable {

    public static final long serialVersionUID = Registry.SerialVersions.KILL_COMMAND;

    public KillCommand() {
    }
}
