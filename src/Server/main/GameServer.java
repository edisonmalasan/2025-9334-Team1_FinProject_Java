package Server.main;

import Client.WhatsTheWord.client.admin.AdminService;
import Client.WhatsTheWord.client.admin.AdminServiceHelper;
import Server.WhatsTheWord.client.player.PlayerService;
import Server.WhatsTheWord.client.player.PlayerServiceHelper;
import Server.WhatsTheWord.game_logic.Game;
import Server.WhatsTheWord.game_logic.GameHelper;
import Server.controller.GameImpl;
import Server.service.AdminRequestService;
import org.omg.CosNaming.*;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import Server.service.PlayerRequestService;

import java.util.Properties;

public class GameServer {
    public static ORB orb;
    public static void main(String[] args) {
        try {
            // Initialize properties, host and port
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialHost","localhost");
            props.put("org.omg.CORBA.ORBInitialPort","10050");

            orb = ORB.init(new String[]{}, props);
// get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
// create servant and register it with the ORB
            GameImpl gameImpl = new GameImpl();
            PlayerRequestService playerRequestService = new PlayerRequestService();
            AdminRequestService adminRequestService = new AdminRequestService();
// get object reference from the servant
            org.omg.CORBA.Object gameRef = rootpoa.servant_to_reference(gameImpl);
            org.omg.CORBA.Object playerServiceRef = rootpoa.servant_to_reference(playerRequestService);
            org.omg.CORBA.Object adminServiceRef = rootpoa.servant_to_reference(adminRequestService);

            Game gameHref = GameHelper.narrow(gameRef);
            PlayerService playerServiceHref = PlayerServiceHelper.narrow(playerServiceRef);
            AdminService adminServiceHref = AdminServiceHelper.narrow(adminServiceRef);
// get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
// Use NamingContextExt which is part of the Interoperable
// Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
// bind the Object Reference in Naming
            String gameName = "Game";
            String playerServiceName = "PlayerService";
            String adminServiceName = "AdminService";

            NameComponent[] gamePath = ncRef.to_name(gameName);
            NameComponent[] playerServicePath = ncRef.to_name(playerServiceName);
            NameComponent[] adminServicePath = ncRef.to_name(adminServiceName);

            ncRef.rebind(gamePath, gameHref);
            ncRef.rebind(playerServicePath, playerServiceHref);
            ncRef.rebind(adminServicePath, adminServiceHref);
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
