package gui.controlador;

import alertas.Alerta;
import alertas.TipoAlerta;
import com.rabbitmq.client.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import mensaxe.Mensaxe;
import mensaxe.TipoMensaxe;
import servidor.Servidor;

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
    private ListView<String> listaAlertasCompra;
    @FXML
    private ListView<String> listaAlertasVenta;
    @FXML
    private TextField txtValor;
    @FXML
    private Label labelNomeUser;
    @FXML
    private ListView<String> listaAlertasEstablecidas;

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
        labelNomeUser.setText(nomeUser);
        try {
            conexion = factory.newConnection();
            canal = conexion.createChannel();

            // Declaramos a cola
            canal.queueDeclare(nomeUser, false, false, true, null);

        } catch (IOException | TimeoutException ex) {
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
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            // Lemos a mensaxe
            ByteArrayInputStream bis = new ByteArrayInputStream(delivery.getBody());
            try (ObjectInputStream is = new ObjectInputStream(bis)) {
                try {
                    Mensaxe men = (Mensaxe) is.readObject();
                    interpretarMensaxe(men);
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Erro lendo a mensaxe");
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        try {
            canal.basicConsume(nomeUser, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println("Erro lendo o mensaxe coas empresas");
            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void accionBtnAlerta(ActionEvent event) {
        TipoAlerta t = TipoAlerta.valueOf(comboTipoAlerta.getSelectionModel().getSelectedItem());
        String e = comboEmpresa.getSelectionModel().getSelectedItem();
        float valor = Float.parseFloat(txtValor.getText());

        Alerta alerta = new Alerta(t, e, valor, nomeUser);
        Mensaxe m = new Mensaxe(TipoMensaxe.PONHER_ALERTA, alerta);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();

        try {
            try (ObjectOutputStream os = new ObjectOutputStream(bs)) {
                os.writeObject(m);
                canal.basicPublish("", COLA_SERVIDOR, null, bs.toByteArray());
                String al = m.getAlerta().getTipo() + " - " + m.getAlerta().getEmpresa() + ": " + alerta.getValor() + "€";
                listaAlertasEstablecidas.getItems().add(0, al);
            }
        } catch (IOException ex) {
            System.out.println("Erro publicando unha nova alerta.");
            Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtValor.clear();
        btnCrearAlerta.setDisable(true);

    }

    @FXML
    private void numeroEscrito(KeyEvent event) {
        if (isFloatPositive(txtValor.getText())) {
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

    private void interpretarMensaxe(Mensaxe m) {
        switch (m.getTipo()) {
            case SAUDO -> {
                String mensaxe = m.getInfo();
                // Actualizamos a UI dende o fio principal de JavaFx
                Platform.runLater(() -> {
                    comboEmpresa.setItems(FXCollections.observableArrayList(mensaxe.split("/")));
                    comboEmpresa.getSelectionModel().selectFirst();
                });

            }
            case NOTIFICAR_ALERTA -> {
                Alerta a = m.getAlerta();
                // Actualizamos a UI dende o fio principal de JavaFx
                Platform.runLater(() -> {
                    engadirAlerta(a, m.getInfo());
                    
                    List<String> list = listaAlertasEstablecidas.getItems();
                    String alerta = a.getTipo() + " - " + a.getEmpresa() + ": " + a.getValor() + "€";
                    for(int i = list.size() - 1; i >= 0; i--){
                        if(alerta.equals(list.get(i))){
                            listaAlertasEstablecidas.getItems().remove(i);
                            break;
                        }
                    }
                });
            }
        }
    }

    private boolean isFloatPositive(String cadea) {
        try {
            float f = Float.parseFloat(cadea);
            if (f < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public void engadirAlerta(Alerta alerta, String valor) {
        String m = alerta.getEmpresa() + ": " + alerta.getValor() + "€ (valor actual: " + valor + "€)";
        if (alerta.getTipo().compareTo(TipoAlerta.COMPRA) == 0) {
            listaAlertasCompra.getItems().add(0, m);
        } else {
            listaAlertasVenta.getItems().add(0, m);
        }
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

}
