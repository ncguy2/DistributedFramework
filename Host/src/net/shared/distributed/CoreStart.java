package net.shared.distributed;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.capabilities.Capabilities;
import net.shared.distributed.core.Core;
import net.shared.distributed.distributor.Distributor;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.receptor.Receptor;

import java.io.IOException;

public class CoreStart {

    public static Core core;
    public static Distributor distributor;
    public static Receptor receptor;

    public static void main(String... args) {
        core = new Core();
        distributor = new Distributor(core);
        receptor = new Receptor(core, distributor);



        try {
            receptor.StartListening(Registry.TCP_PORT, Registry.UDP_PORT, CoreStart::NodeCommFunctions);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try{
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        new StringToUpperFunction(distributor).Invoke("asdfghjkl");

    }

    public static void NodeCommFunctions(Connection conn, Object obj) {
        Logger.instance().Verbose(obj.getClass().getSimpleName()+", "+obj.toString());
        if(obj instanceof RoutedResponse)
            distributor.HandleRoutedResponse(conn, (RoutedResponse) obj);
        else
            Capabilities.instance().Accept(conn, obj);
    }

}
