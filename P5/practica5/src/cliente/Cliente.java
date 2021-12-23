package cliente;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Anton Gomez e Crsitina Lopez
 */
public class Cliente extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Cliente.class.getResource("/gui/vista/ventaLogin.fxml"));
        Pane root = (Pane) loader.load();

        primaryStage.setTitle("Falando");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
}
