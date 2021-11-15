package gui.controlador;

import alertas.Alerta;
import alertas.TipoAlerta;
import cliente.RecibidorAlertas;
import com.rabbitmq.client.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import mensaxe.Mensaxe;
import mensaxe.TipoMensaxe;

/**
 * FXML Controller class
 *
 * @author Anton Gomez Lopez
 */
public class ControladorAlertas implements Initializable {

    protected final static String COLA_SERVIDOR = "servidor";

    private ConnectionFactory factory;
    private Connection conexion;
    private Channel canal;
    private String nomeUser;

    @FXML
    private Button btnCrearAlerta;
    @FXML
    private ComboBox<String> comboEmpresa;
    @FXML
    private ComboBox<String> comboTipoAlerta;
    @FXML
    private ListView<Alerta> listaAlertas;
    @FXML
    private TextField txtValor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        factory = new ConnectionFactory();
        factory.setHost("localhost");

        comboTipoAlerta.setItems(FXCollections.observableArrayList(TipoAlerta.COMPRA.name(), TipoAlerta.VENTA.name()));
        comboTipoAlerta.getSelectionModel().selectFirst();

        btnCrearAlerta.setDisable(true);

    }

    // Invocamos este metodo unha vez que teñamos o nome da cola disponhible
    // (nome do usuario). Conectamonos co servidor para que nos mande o 
    // nome das empresas do IBEX e engada a cola a lista de colas
    public void recibirEmpresas() {
        try {
            conexion = factory.newConnection();
            canal = conexion.createChannel();

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            // Declaramos unha cola para o cliente
            canal.queueDeclare(nomeUser, false, false, false, null);
        } catch (IOException ex) {
            System.out.println("Erro declarando a cola do cliente.");
            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Enviamos un saudo ao servidor para que conheza o nome da cola
        Mensaxe m = new Mensaxe(TipoMensaxe.SAUDO, nomeUser);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {
            // Declaramos a cola do servidor
            canal.queueDeclare(COLA_SERVIDOR, false, false, false, null);
            try (ObjectOutputStream os = new ObjectOutputStream(bs)) {
                os.writeObject(m);
            }
            canal.basicPublish("", COLA_SERVIDOR, null, bs.toByteArray());
        } catch (IOException ex) {
            System.out.println("Erro enviando a mensaxe de saudo.");
            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Recibimos a mensaxe inicial coas empresas
        try {

            GetResponse response;
            do {
                response = canal.basicGet(nomeUser, true);
            } while (response == null);

            ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());
            try (ObjectInputStream is = new ObjectInputStream(bis)) {
                m = (Mensaxe) is.readObject();
            }
            String mensaxe = m.getInfo();
            comboEmpresa.setItems(FXCollections.observableArrayList(mensaxe.split("/")));

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro lendo o mensaxe coas empresas");
            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, e);
        }

        comboEmpresa.getSelectionModel().selectFirst();

        // Iniciamos o fio que recibe as alertas
        RecibidorAlertas recv = new RecibidorAlertas(conexion, canal, nomeUser, this);
        recv.setDaemon(true);
        recv.start();
    }

    @FXML
    private void accionBtnAlerta(ActionEvent event) {
        TipoAlerta t = TipoAlerta.valueOf(comboTipoAlerta.getSelectionModel().getSelectedItem());
        String e = comboEmpresa.getSelectionModel().getSelectedItem();
        int valor = Integer.parseInt(txtValor.getText());

        Alerta alerta = new Alerta(t, e, valor, nomeUser);
        Mensaxe m = new Mensaxe(TipoMensaxe.PONHER_ALERTA, alerta);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();

        try {
            try (ObjectOutputStream os = new ObjectOutputStream(bs)) {
                os.writeObject(m);
            }
            canal.basicPublish("", COLA_SERVIDOR, null, bs.toByteArray());
        } catch (IOException ex) {
            System.out.println("Erro publicando unha nova alerta.");
            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtValor.clear();
        btnCrearAlerta.setDisable(true);

    }

    @FXML
    private void numeroEscrito(KeyEvent event) {
        if (isNumeric(txtValor.getText())) {
            btnCrearAlerta.setDisable(false);
        } else {
            btnCrearAlerta.setDisable(true);
        }
    }

    @FXML
    private void enviarAlerta(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnCrearAlerta.fire();
        }
    }

    public void engadirAlerta(Alerta alerta) {
        listaAlertas.getItems().add(alerta);
    }

    public void setFactory(ConnectionFactory factory) {
        this.factory = factory;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public void setCanal(Channel canal) {
        this.canal = canal;
    }

    public Connection getConexion() {
        return conexion;
    }

    public Channel getCanal() {
        return canal;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    private boolean isNumeric(String cadea) {
        try {
            Integer.parseInt(cadea);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
