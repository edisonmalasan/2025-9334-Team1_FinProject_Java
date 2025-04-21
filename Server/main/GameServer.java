package main;

import common.client.player.PlayerService;
import common.client.player.PlayerServiceHelper;
import common.game_logic.Game;
import common.game_logic.GameHelper;
import controller.GameImpl;
import org.omg.CosNaming.*;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import service.PlayerRequestService;

public class GameServer {
    public static ORB orb;
    public static void main(String[] args) {
        try {
            orb = ORB.init(args, null);
// get reference to rootpoa & activate the POAManager
            POA rootpoa =
                    POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
// create servant and register it with the ORB
            GameImpl gameImpl = new GameImpl();
            PlayerRequestService playerRequestService = new PlayerRequestService();
// get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(gameImpl);
            org.omg.CORBA.Object ref1 = rootpoa.servant_to_reference(playerRequestService);
            Game href = GameHelper.narrow(ref);
            PlayerService href1 = PlayerServiceHelper.narrow(ref1);
// get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
// Use NamingContextExt which is part of the Interoperable
// Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
// bind the Object Reference in Naming
            String gameName = "Game";
            String playerServiceName = "PlayerService";
            NameComponent[] path = ncRef.to_name(gameName);
            NameComponent[] path1 = ncRef.to_name(playerServiceName);
            ncRef.rebind(path, href);
            ncRef.rebind(path1, href1);
            System.out.println("GameServer ready and waiting ...");
// wait for invocations from clients
            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("GameServer Exiting...");
    }


}

//orbd –ORBInitialPort 10050 –ORBInitialHost localhost
