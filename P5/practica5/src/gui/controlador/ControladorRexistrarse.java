package gui.controlador;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import servidor.ServidorInterfaz;

/**
 * FXML Controller class
 *
 * @author Esther
 */
public class ControladorRexistrarse implements Initializable {

    private ServidorInterfaz servidor;

    @FXML
    private Label labelPrincipal;
    @FXML
    private TextField txtNomeUser;
    @FXML
    private PasswordField txtContrasinal1;
    @FXML
    private PasswordField txtContrasinal2;
    @FXML
    private Button btnRexistrar;
    @FXML
    private Label labErro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labErro.setVisible(false);
        btnRexistrar.setDisable(true);
    }

    @FXML
    private void enviarMensaxe(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnRexistrar.fire();
        }
    }

    @FXML
    private void letraEscrita(KeyEvent event) {
        // Non permitimos nomes de usuario menores que 3 caracteres
        if (txtNomeUser.getText().isBlank() || txtContrasinal1.getText().isBlank()
                || txtContrasinal2.getText().isBlank()
                || (txtNomeUser.getText().length() < 3)) {

            btnRexistrar.setDisable(true);
        } else {
            btnRexistrar.setDisable(false);
        }

        labErro.setVisible(false);
    }

    @FXML
    private void accionBtnRexistrarse(ActionEvent event) {
        if (!txtContrasinal1.getText().equals(txtContrasinal2.getText())) {
            labErro.setText("Os contrasinais deben coincidir");
            labErro.setVisible(true);
            txtContrasinal1.clear();
            txtContrasinal2.clear();
        } else if (txtContrasinal1.getText().length() < 3) {
            labErro.setText("O contrasinal debe ter como minimo 3 caracteres");
            labErro.setVisible(true);
        }  else if (txtNomeUser.getText().length() < 3) {
            labErro.setText("O nome de usuario debe ter como minimo 3 caracteres");
            labErro.setVisible(true);
        }else {
            try {
                if (servidor.rexistrarse(txtNomeUser.getText(), txtContrasinal1.getText())) {
                    ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                } else {
                    labErro.setText("Ese nome de usuario xa está rexistrado");
                    labErro.setVisible(true);
                    txtNomeUser.clear();
                    txtContrasinal1.clear();
                    txtContrasinal2.clear();
                }
            } catch (RemoteException ex) {
                Logger.getLogger(ControladorRexistrarse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void setServidor(ServidorInterfaz servidor) {
        this.servidor = servidor;
    }

}
