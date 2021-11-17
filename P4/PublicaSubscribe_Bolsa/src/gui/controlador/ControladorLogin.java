package gui.controlador;

import cliente.Cliente;
import static gui.controlador.ControladorAlertas.COLA_SERVIDOR;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mensaxe.Mensaxe;
import mensaxe.TipoMensaxe;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ControladorLogin implements Initializable {

    @FXML
    private TextField txtNomeUser;
    @FXML
    private Button btnConectar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnConectar.setDisable(true);
    }

    // Permitimos activar o boton pusando enter
    @FXML
    private void enviarMensaxe(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            btnConectar.fire();
        }
    }

    @FXML
    private void letraEscrita(KeyEvent event) {
        // So se permiten nomes de usuario menores de 10 caracteres 
        if (txtNomeUser.getText().length() > 10) {
            String s = txtNomeUser.getText().substring(0, 10);
            txtNomeUser.setText(s);
            txtNomeUser.positionCaret(txtNomeUser.getText().length());
        }

        // Non se permiten barras no nome de usuario
        if (event.getCharacter().equals(" ")) {
            String s = txtNomeUser.getText().substring(0, txtNomeUser.getText().length() - 1);
            txtNomeUser.setText(s);
            txtNomeUser.positionCaret(txtNomeUser.getText().length());
        }

        // So activamos o boton de conectar o nome de usuario ten algo
        if (txtNomeUser.getText().isBlank()) {
            btnConectar.setDisable(true);
        } else {
            btnConectar.setDisable(false);
        }
    }

    @FXML
    private void accionBtnConectar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Cliente.class.getResource("/gui/vista/ventaAlertas.fxml"));
            Pane root = (Pane) loader.load();
            ControladorAlertas c = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Alertas Bolsa Madrid");
            stage.setScene(new Scene(root));

            // Pechamos o canal e a conexion cando o cliente peche a venta
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        // Enviamos unha mensaxe de despedida
                        Mensaxe m = new Mensaxe(TipoMensaxe.DESPEDIDA, c.getNomeUser());
                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                        try {

                            try (ObjectOutputStream os = new ObjectOutputStream(bs)) {
                                os.writeObject(m);
                            }
                            c.getCanal().basicPublish("", COLA_SERVIDOR, null, bs.toByteArray());
                        } catch (IOException ex) {
                            System.out.println("Erro enviando alerta de notificacion");
                            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        // Pechamos o canal que usan ambos fios do cliente
                        c.getCanal().close();
                        c.getConexion().close();
                    } catch (IOException | TimeoutException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            c.setNomeUser(txtNomeUser.getText());
            c.recibirEmpresas();

            // Pechamos a venta de Login
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
