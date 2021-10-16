package gui.controlador;

import chatgrupal.ChatGrupal;
import static chatgrupal.ChatGrupal.*;
import chatgrupal.Listener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Anton Gomez Lopez
 */
public class VentaLoginControlador implements Initializable {

    @FXML
    private Label labelPrincipal;
    @FXML
    private Button btnConectar;
    @FXML
    private TextField txtNomeUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnConectar.setDisable(true);
    }

    @FXML
    private void accionBtnConectar(ActionEvent event) throws IOException {
        Stage stage = new Stage(StageStyle.DECORATED);

        FXMLLoader loader = new FXMLLoader(ChatGrupal.class.getResource("/gui/vista/ventaChat.fxml"));
        Pane root = (Pane) loader.load();
        VentaChatControlador c = loader.getController();

        // Introducimos o nome de usuario
        c.setNomeUser(txtNomeUser.getText());

        stage.setTitle("Chat grupal");
        stage.setScene(new Scene(root));

        // Pechamos a venta de Login
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        // Creamos o socket de recepcion
        crearListener(c, stage);

        // Amosamos a venta do chat
        stage.show();

    }

    private void crearListener(VentaChatControlador c, Stage stage) throws IOException {

        // Creamos un novo MulticastSocket no porto indicado
        MulticastSocket socket = new MulticastSocket(PORTO);
        InetAddress mcastaddr = InetAddress.getByName(INET);
        InetSocketAddress group = new InetSocketAddress(mcastaddr, PORTO);
        NetworkInterface netIf = NetworkInterface.getByInetAddress(mcastaddr);
        socket.joinGroup(group, netIf);

        // Asignamoslle o socket ao controlador
        c.setSocket(socket);
        c.setGroup(group);

        // Executamos o codigo correspondente a recepcion de mensaxes noutro fio
        Listener listener = new Listener(c, socket);
        // Finalizamos o fio se a aplicacion se pecha
        listener.setDaemon(true);
        // Comeza a execucion do fio
        listener.start();

        // Abandonamos o grupo
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    socket.leaveGroup(group, netIf);
                } catch (IOException ex) {
                    Logger.getLogger(VentaChatControlador.class.getName()).log(Level.SEVERE, null, ex);
                }finally {
                    socket.close();
                }
            }
        });

    }

    @FXML
    private void letraEscrita(KeyEvent e) {

        // So se permiten nomes de usuario menores de 10 caracteres 
        if (txtNomeUser.getText().length() > 10) {
            String s = txtNomeUser.getText().substring(0, 10);
            txtNomeUser.setText(s);
            txtNomeUser.positionCaret(txtNomeUser.getText().length());
        }

        // Non se permiten barras no nome de usuario
        if (e.getCharacter().equals(":")) {
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

    // Permitimos activar o boton pusando enter
    @FXML
    private void enviarMensaxe(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            btnConectar.fire();
        }
    }

}
