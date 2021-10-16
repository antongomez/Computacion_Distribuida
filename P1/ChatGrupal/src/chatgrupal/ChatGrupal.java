package chatgrupal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Anton Gomez Lopez
 */
public class ChatGrupal extends Application {

    public static final String INET = "224.0.0.100";
    public static final int PORTO = 6703;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ChatGrupal.class.getResource("/gui/vista/ventaLogin.fxml"));
        Pane root = (Pane) loader.load();

        primaryStage.setTitle("Chat grupal");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        primaryStage.show();
    }

}
