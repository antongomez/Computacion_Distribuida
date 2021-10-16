package gui.controlador;

import chatgrupal.Enviador;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import mensaxes.Mensaxe;

/**
 * FXML Controller class
 *
 * @author Anton Gomez Lopez
 */
public class VentaChatControlador implements Initializable {

    private String nomeUser;
    private MulticastSocket socket;
    protected InetSocketAddress group;

    @FXML
    private Button btnEnviar;
    @FXML
    private TextArea txtareaMensaxe;
    @FXML
    private ListView<HBox> chatPane;
    @FXML
    private ImageView labelChat;
    @FXML
    private Label labelPrincipal;
    @FXML
    private Label labelNomeUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void actBtnEnviar(ActionEvent event) {

        if (!txtareaMensaxe.getText().isBlank()) {
            // Executamos o codigo correspondente a recepcion de mensaxes noutro fio
            Enviador enviador = new Enviador(socket, txtareaMensaxe.getText(), nomeUser, group);
            // Finalizamos o fio se a aplicacion se pecha
            enviador.setDaemon(true);
            // Comeza a execucion do fio
            enviador.start();
        }

        // Limamos a caixa onde se escribe
        txtareaMensaxe.clear();
    }

    public void engadirAoChat(Mensaxe msx) {
        Label lb = new Label();

        lb.setWrapText(true);
        lb.getStyleClass().add("mnx");
        HBox x = new HBox();
        x.setMaxWidth(chatPane.getWidth() - 20);

        if ((msx.getNomeUser() == null) || !(msx.getNomeUser().equals(nomeUser))) {
            lb.setText(msx.getNomeUser() + ": " + msx.getMsx());
            x.setAlignment(Pos.CENTER_LEFT);
            lb.getStyleClass().add("mnxRecibido");
        } else {
            lb.setText(msx.getMsx());
            x.setAlignment(Pos.CENTER_RIGHT);
            lb.getStyleClass().add("mnxEnviado");
        }

        x.getChildren().add(lb);

        int index = chatPane.getItems().size();
        chatPane.getItems().add(x);

        chatPane.scrollTo(chatPane.getItems().get(index)); // Solucion parcial
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
        labelNomeUser.setText(nomeUser);
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    public void setSocket(MulticastSocket socket) {
        this.socket = socket;
    }

    public InetSocketAddress getGroup() {
        return group;
    }

    public void setGroup(InetSocketAddress group) {
        this.group = group;
    }

}
