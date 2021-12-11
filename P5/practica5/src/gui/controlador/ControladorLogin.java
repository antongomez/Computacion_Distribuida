package gui.controlador;

import cliente.ClienteImpl;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import servidor.ServidorInterfaz;

/**
 * FXML Controller class
 *
 * @author Anton Gomez e Cristina Lopez
 */
public class ControladorLogin implements Initializable {

    ServidorInterfaz servidor;

    @FXML
    private TextField txtNomeUser;
    @FXML
    private PasswordField txtContrasinal;
    @FXML
    private Button btnConectar;
    @FXML
    private Label labErro;
    @FXML
    private Label labelPrincipal;
    @FXML
    private Button btnRexistrarse;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnConectar.setDisable(true);
        labErro.setVisible(false);
        conectarServidor();
    }

    @FXML
    private void accionBtnConectar(ActionEvent event) {
        try {
            Stage stage = new Stage(StageStyle.DECORATED);

            FXMLLoader loader = new FXMLLoader(ControladorLogin.class.getResource("/gui/vista/ventaChat.fxml"));
            Pane root = (Pane) loader.load();
            ControladorChat controlador = loader.getController();

            if (iniciarSesion(controlador, txtNomeUser.getText(), txtContrasinal.getText())) {
                stage.setTitle("Falando");
                stage.setScene(new Scene(root));

                // Abandonamos o grupo
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        try {
                            controlador.getServidor().desconectarse(controlador.getContrasinal(), controlador.getCliente());
                        } catch (RemoteException ex) {
                            System.out.println("Non se puido localizar algo (ControladorLogin)");
                            Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                // Pechamos a venta de Login
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

                // Amosamos a venta do chat
                stage.show();
            } else {
                txtContrasinal.clear();
                // Mostrar unha etiqueta bonita
                // La rusa peligrosa te va a matar
            }

        } catch (IOException ex) {
            Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void conectarServidor() {
        String registryURL = "rmi://localhost:6000/callback";
        try {
            servidor = (ServidorInterfaz) Naming.lookup(registryURL);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean iniciarSesion(ControladorChat controlador, String nomeUser, String contrasinal) {
        try {

            controlador.setServidor(servidor);

            controlador.setCliente(new ClienteImpl(controlador));
            // register for callback

            if (controlador.getServidor().comprobarCredenciais(nomeUser, contrasinal)) {

                controlador.setNomeUser(txtNomeUser.getText());
                controlador.setContrasinal(txtContrasinal.getText());
                controlador.getServidor().conectarse(nomeUser, contrasinal, controlador.getCliente());

            } else {

                labErro.setVisible(true);

                return false;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ControladorChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @FXML
    private void letraEscrita(KeyEvent e) {
        if (txtNomeUser.getText().isBlank() || txtContrasinal.getText().isEmpty()) {
            btnConectar.setDisable(true);
        } else {
            btnConectar.setDisable(false);
        }
        labErro.setVisible(false);
    }

    // Permitimos activar o boton pusando enter
    @FXML
    private void enviarMensaxe(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            btnConectar.fire();
        }
    }

    @FXML
    private void accionBtnRexistrarse(ActionEvent event) {

        Stage stage = new Stage(StageStyle.DECORATED);

        FXMLLoader loader = new FXMLLoader(ControladorLogin.class.getResource("/gui/vista/ventaRexistrarse.fxml"));
        try {
            Pane root = (Pane) loader.load();
            ControladorRexistrarse controlador = loader.getController();
            stage.setTitle("Falando");
            stage.setScene(new Scene(root));
            controlador.setServidor(servidor);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
