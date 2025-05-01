package Client.main;

import Client.WhatsTheWord.client.admin.AdminService;
import Client.WhatsTheWord.client.admin.AdminServiceHelper;
import Client.common.callback.ClientCallbackImpl;
import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.ClientCallbackHelper;
import Client.WhatsTheWord.client.player.PlayerRequestType;
import Client.WhatsTheWord.client.player.PlayerService;
import Client.WhatsTheWord.client.player.PlayerServiceHelper;
import Client.WhatsTheWord.game_logic.Game;
import Client.WhatsTheWord.game_logic.GameHelper;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.Player.controller.GameController;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Properties;

public class Client {
    static ORB orb;
    public static Game game;
    public static ClientCallback callback;
    static Player player;
    public static PlayerService playerService;
    public static AdminService adminService;
    public static void main(String[] args) {
        try {
// Initialize properties, host and port
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialHost", "localhost");
            props.put("org.omg.CORBA.ORBInitialPort", "10050");

            orb = ORB.init(args, props);
// get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
// Use NamingContextExt instead of NamingContext. This is part
// of the Interoperable naming Service.
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
// resolve the Object Reference in Naming

            String gameName = "Game";
            String playerServiceName = "PlayerService";
            String adminServiceName = "AdminService";

            game = GameHelper.narrow(ncRef.resolve_str(gameName));
            playerService = PlayerServiceHelper.narrow(ncRef.resolve_str(playerServiceName));
            adminService = AdminServiceHelper.narrow(ncRef.resolve_str(adminServiceName));

            ClientCallbackImpl callbackImpl = new ClientCallbackImpl();
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(callbackImpl);
            callback = ClientCallbackHelper.narrow(ref);

            player = new Player(0,"Test","Tester",0,0,-1,false);
            GameController gameController = new GameController();
            callbackImpl.addObserver(gameController);
            playerService.request(PlayerRequestType.START_GAME,player,callback);

            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
