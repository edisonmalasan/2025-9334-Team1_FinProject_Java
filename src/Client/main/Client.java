package Client.main;

import Client.Player.controller.PlayerLogin;
import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.admin.AdminService;
import Client.WhatsTheWord.client.admin.AdminServiceHelper;
import Client.common.ClientControllerObserver;
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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Properties;

import static javafx.application.Application.launch;

public class Client extends Application {
    static ORB orb;
    public static Game game;
    public static ClientCallback callback;
    public static ClientCallbackImpl callbackImpl = new ClientCallbackImpl();
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

            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(callbackImpl);
            callback = ClientCallbackHelper.narrow(ref);

            player = new Player(0,"Test","Tester",0,0,-1,false);

            //GameController gameController = new GameController();
            //callbackImpl.addObserver(gameController);
            //playerService.request(PlayerRequestType.START_GAME,player,callback);

            launch(args);

            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) throws Exception {
        try {
            ViewManager.initialize(primaryStage);
            primaryStage.setTitle("What's The Word Game");
            primaryStage.setResizable(false);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void start(Stage primaryStage) throws Exception {
//        PlayerLogin playerLogin = new PlayerLogin();
//        callbackImpl.addObserver(playerLogin);
//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Client/Player/view/PlayerLogin.fxml"));
//        Scene scene = new Scene(root);
//        primaryStage.setTitle("Test");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        primaryStage.setResizable(false);
//    }
}
