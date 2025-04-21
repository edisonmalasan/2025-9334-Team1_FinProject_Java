package main;

import callback.ClientCallbackImpl;
import common.client.ClientCallback;
import common.client.ClientCallbackHelper;
import common.client.player.PlayerRequestType;
import common.client.player.PlayerService;
import common.client.player.PlayerServiceHelper;
import common.game_logic.Game;
import common.game_logic.GameHelper;
import common.referenceClasses.Player;
import controller.GameController;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Client1 {
    static Game game;
    static PlayerService playerService;
    static ClientCallback callback;
    static Player player;
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
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
            game = GameHelper.narrow(ncRef.resolve_str(gameName));
            PlayerService playerService = PlayerServiceHelper.narrow(ncRef.resolve_str(playerServiceName));

            ClientCallbackImpl callbackImpl = new ClientCallbackImpl();
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(callbackImpl);
            callback = ClientCallbackHelper.narrow(ref);

            player = new Player(0,"Test1","Tester",0,0,-1,false);
            GameController gameController = new GameController(player);
            callbackImpl.addObserver(gameController);
            playerService.request(PlayerRequestType.START_GAME,player,callback);

            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
