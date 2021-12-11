package gui.controlador;

import cliente.ClienteInterfaz;
import cliente.Mensaxe;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import servidor.ServidorInterfaz;

/**
 * FXML Controller class
 *
 * @author Anton Gomez e Cristina Lopez
 */
public class ControladorChat implements Initializable {

    private String nomeUser;
    private int numFotoPerfil;
    private String contrasinal;
    private ServidorInterfaz servidor;
    private ClienteInterfaz cliente;
    private HashMap<String, VBox> listasChats;                  // Tenhense en tonta todas as posibles carreiras criticas 
    private HashMap<String, ClienteInterfaz> usuariosEnLinha;   // Tenhense en conta todas as posibles carreiras criticas
    private Stage ventaNotif;

    @FXML
    private Label labelPrincipal;
    @FXML
    private Label labelNomeUser;
    @FXML
    private TextArea txtareaMensaxe;
    @FXML
    private Button btnEnviar;
    @FXML
    private ListView<VBox> listaEnLinha;            // So hai carreiras criticas ao iterar a lista, engadir e borrar
    @FXML
    private StackPane xestorListas;
    @FXML
    private TextField txtBuscador;
    @FXML
    private ListView<String> listaBuscaUsuarios;    // Chamase sempre dende o fio principal (non hai carreiras criticas)
    @FXML
    private ListView<HBox> listaSolicitudes;        // Chamase sempre dende o fio principal (non hai carreiras criticas)
    @FXML
    private Button btnSolicitarAmizade;
    @FXML
    private Label lbInfo;
    @FXML
    private ImageView fotoPerfil;
    @FXML
    private Tab tabSoli;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listasChats = new HashMap<>();
        usuariosEnLinha = new HashMap<>();
        btnEnviar.setDisable(true);
        btnSolicitarAmizade.setDisable(true);

        // Xeramos aleatoriamente un numero entre 1 e 9 para escoller unha foto
        Random r = new Random();
        numFotoPerfil = r.nextInt(9) + 1;
        fotoPerfil.setImage(new Image("recursos/" + numFotoPerfil + ".png"));

        //Crease a venta de notificacions
        ventaNotif = new Stage(StageStyle.DECORATED);
        FXMLLoader loader = new FXMLLoader(ControladorChat.class.getResource("/gui/vista/ventaNotificacion.fxml"));
        try {
            Pane root = (Pane) loader.load();
            ventaNotif.setTitle("Falando");
            ventaNotif.setScene(new Scene(root));
            ventaNotif.setResizable(false);

        } catch (IOException ex) {
            Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void actBtnEnviar(ActionEvent event) throws RemoteException {
        if (!txtareaMensaxe.getText().isEmpty()) {
            int tam = listaEnLinha.getChildrenUnmodifiable().size();
            // Collemos a lista que esta enriba de todo.
            VBox caixa = (VBox) listaEnLinha.getSelectionModel().getSelectedItems().get(tam - 1);
            String idUsuarioReceptor = obterCampoEnLinhaNomeUser(caixa).getText();
            ClienteInterfaz usuarioReceptor;
            synchronized (usuariosEnLinha) {
                usuarioReceptor = usuariosEnLinha.get(idUsuarioReceptor);
            }
            usuarioReceptor.recibirMensaxe(txtareaMensaxe.getText(), nomeUser);

            Mensaxe m = new Mensaxe(txtareaMensaxe.getText(), idUsuarioReceptor, nomeUser);
            engadirAoChat(m);

            txtareaMensaxe.clear();

        }

    }

    @FXML
    private void seleccionarChat(MouseEvent event) {
        // Obtemos o elemento seleccionado na lista
        if (!listaEnLinha.getSelectionModel().getSelectedItems().isEmpty()) {

            VBox caixa = (VBox) listaEnLinha.getSelectionModel().getSelectedItem();
            // Obtemos o nome que esta dentro da etiqueta dentro da caixa
            String nome = obterCampoEnLinhaNomeUser(caixa).getText();

            // Eliminamos o texto do numero de mensaxes sen ler
            obterCampoEnLinhaNMensaxesSenLer(caixa).setText("");
            obterCampoEnLinhaNMensaxesSenLer(caixa).setVisible(false);

            VBox chat;
            synchronized (listasChats) {
                chat = listasChats.get(nome);
            }
            // Traemos o chat para adiante
            xestorListas.getChildren().remove(chat);
            xestorListas.getChildren().add(chat);

            btnEnviar.setDisable(false);
        }

    }

    @FXML
    private void seleccionarUsuario(MouseEvent event) {
        //String usuario = listaBuscaUsuarios.getSelectionModel().getSelectedItem();
        btnSolicitarAmizade.setDisable(listaBuscaUsuarios.getSelectionModel().isEmpty());
    }

    @FXML
    private void buscar(KeyEvent event) {
        realizarBusqueda();
        btnSolicitarAmizade.setDisable(true);
    }

    private void realizarBusqueda() {

        if (txtBuscador.getText().length() > 2) {
            int tam = listaBuscaUsuarios.getItems().size();
            for (int i = 0; i < tam; i++) {
                listaBuscaUsuarios.getItems().remove(0);
            }
            List<String> listaUsuarios = new ArrayList<>();
            try {
                listaUsuarios = servidor.buscarUsuarioSolicitarAmizade(txtBuscador.getText(), nomeUser, contrasinal);
            } catch (RemoteException ex) {
                Logger.getLogger(ControladorChat.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (String id : listaUsuarios) {
                listaBuscaUsuarios.getItems().add(id);
            }
        } else {
            int tam = listaBuscaUsuarios.getItems().size();
            for (int i = 0; i < tam; i++) {
                listaBuscaUsuarios.getItems().remove(0);
            }
        }
    }

    @FXML
    private void solicitarAmizade(ActionEvent event) {

        String solicitado = listaBuscaUsuarios.getSelectionModel().getSelectedItem();
        try {
            servidor.solicitarAmizade(nomeUser, solicitado, contrasinal);
        } catch (RemoteException ex) {
            Logger.getLogger(ControladorChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        int tam = listaBuscaUsuarios.getItems().size();
        for (int i = 0; i < tam; i++) {
            listaBuscaUsuarios.getItems().remove(0);
        }

        ventaNotif.show();
        btnSolicitarAmizade.setDisable(true);

    }

    @FXML
    private void actSolicitudes(Event event) {
        tabSoli.getStyleClass().add("tabSolicitudesNormal");
    }

    public void recibirSolicitude(String usuario) {

        Button btnAceptar = new Button("Aceptar");
        btnAceptar.getStyleClass().add("btnAceptar");
        Button btnRexeitar = new Button("Rexeitar");
        btnRexeitar.getStyleClass().add("btnRexeitar");
        Label lbSolicitante = new Label(usuario);
        HBox hbLbUsuario = new HBox(lbSolicitante);
        // Caixa con un spacing de 10 e cos elementos necesarios
        HBox caixa = new HBox(10, hbLbUsuario, btnAceptar, btnRexeitar);

        // Accion dos bototns
        btnAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actBtnAceptar(usuario, caixa);
            }
        });
        btnRexeitar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actBtnRexeitar(usuario, caixa);
            }
        });

        // Xa non pode haber carreiras criticas ao editar a interfaz posto que 
        // se executa todo no fio principal
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Editamos as propiedades das caixas
                caixa.setAlignment(Pos.CENTER_LEFT);
                hbLbUsuario.setAlignment(Pos.CENTER_LEFT);
                // Desta forma facemos que o nome de usuario se situe a esquerda e 
                // os botons o mias a dereita posible
                HBox.setHgrow(hbLbUsuario, Priority.ALWAYS);
                // A caixa vertical ponhemoslle un padding para deixar algo de sitio
                // aos lados e por arriba
                caixa.setPadding(new Insets(8));
                // Axustamos o tamanho maximo da etiqueta do nome de usuario
                // Necesitamos sitio para o spacing (2 ocos) para os botons e para o 
                // padding (8 da esquerda e 8 da dereita)
                double tamMax = listaSolicitudes.getPrefWidth() - 2 * 10 - btnAceptar.getWidth() - btnRexeitar.getWidth() - 2 * 8;
                lbSolicitante.setMaxWidth(tamMax);

                listaSolicitudes.getItems().add(caixa);

                //Si se recibe unha solicitude de amizade e se estaba buscando a un usuario, reapitese a
                //busqueda para que non apareza o usuario que realizou a solicitude
                realizarBusqueda();

                // Marcamos a pestana en negrita
                tabSoli.getStyleClass().add("tabSolicitudesNegrita");

            }
        });

    }

    private void actBtnAceptar(String usuario, HBox caixa) {
        try {
            servidor.aceptarSolicitude(usuario, nomeUser, contrasinal);
            listaSolicitudes.getItems().remove(caixa);
        } catch (RemoteException ex) {
            Logger.getLogger(ControladorChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void actBtnRexeitar(String usuario, HBox caixa) {
        try {
            servidor.rexeitarSolicitude(usuario, nomeUser, contrasinal);
            listaSolicitudes.getItems().remove(caixa);
        } catch (RemoteException ex) {
            Logger.getLogger(ControladorChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void engadirAoChat(Mensaxe msx) {
        // Etiqueta na que vai o texto da mensaxe
        Label lb = new Label();
        // Chat da persoa que envia ou recibe a mensaxe
        ListView<HBox> lista;

        // O texto tense que poder cortar se e moi longo
        lb.setWrapText(true);
        // Clase do css das mensaxes
        lb.getStyleClass().add("mnx");
        // Metemos a mensaxe na etiqueta
        lb.setText(msx.getMsx());
        // Caixa horizontal onde se mete a mensaxe
        HBox x = new HBox();

        // Caixa vertical onde se mete na segunda posicion a mensaxe
        // (Isto e para que se vexa cal foi a ultima mensaxe enviada)
        VBox caixa;

        // Variable para indicar o numero de mensaxes sen ler
        Integer nMensaxes;

        // No caso de que sexa unha mensaxe que reciba este cliente, 
        // a mensaxe situarase cunha aliñacion a esquerda
        if (msx.getDestinatario().equals(nomeUser)) {
            // Collemos o chat da persoa que envia a mensaxe
            synchronized (listasChats) {
                lista = obterlistaChats(listasChats.get(msx.getEmisor()));
            }
            x.setAlignment(Pos.CENTER_LEFT);
            lb.getStyleClass().add("mnxRecibido");
            // Obtemos a caixa vertical na lista dos amigos que estan en linha 
            // para actualizr o campo de ultima mensaxe do chat
            caixa = obterCaixaUsuarioListaEnLinha(msx.getEmisor());

            // Actualizamos o numero de mensaxes sen ler
            if (!estaSeleccionadoChatListaEnLinha(caixa)) {
                String num = obterCampoEnLinhaNMensaxesSenLer(caixa).getText();
                if (num.length() > 0) {
                    // Obtemos o numero de mensaxes sen ler e sumamoslle 1
                    nMensaxes = Integer.parseInt(obterCampoEnLinhaNMensaxesSenLer(caixa).getText()) + 1;
                } else {
                    // Caso no que non habia mensaxes sen ler desta persoa
                    nMensaxes = 1;
                }
            } else {
                // No caso de que o chat estea seleccionado non engadimos nada
                nMensaxes = -1;
            }

        } else {
            synchronized (listasChats) {
                lista = obterlistaChats(listasChats.get(msx.getDestinatario()));
            }
            lb.getStyleClass().add("mnxEnviado");
            caixa = obterCaixaUsuarioListaEnLinha(msx.getDestinatario());
            x.setAlignment(Pos.CENTER_RIGHT);

            // So engadimos o numero de mensaxes se este cliente e o que
            // recibe a mensaxe
            nMensaxes = -1;
        }

        // Ponhemoslle un tamaño maximo a etiqueta
        lb.setMaxWidth(lista.getPrefWidth() - 40);

        // Engadismoslle a etiqueta coa mensaxe a caixa horizontal que se 
        // vai meter no chat
        x.getChildren().add(lb);

        // Obtemos o campo (label) da ultima mensaxe enviada ou recibida
        Label lbUltimaMensaxe = obterCampoEnLinhaUltimaMensaxeEnviada(caixa);

        // Collemos a posicion da ultima mensaxe do chat para baixar automaticamente
        // ata a ultima mensaxe
        int index = lista.getItems().size();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Engadimos a mensaxe ao chat
                lista.getItems().add(x);
                lista.scrollTo(lista.getItems().get(index)); // Solucion parcial

                if (caixa != null) {
                    // Actualizamos o campo ultima mensaxe do chat na lista de 
                    // amigos en linha
                    lbUltimaMensaxe.setText(msx.getMsx());
                } else {
                    System.out.println("Erro, non funciona o de engadir a ultima mensaxe.\n");
                }

                if (nMensaxes != -1) {
                    obterCampoEnLinhaNMensaxesSenLer(caixa).setText(nMensaxes.toString());
                    obterCampoEnLinhaNMensaxesSenLer(caixa).setVisible(true);
                }

                lbInfo.setText("Tes novas mensaxes!");
            }
        });

    }

    private boolean estaSeleccionadoChatListaEnLinha(VBox caixaUser) {
        if (listaEnLinha.getSelectionModel().isEmpty()) { // Non hai nada
            return false;
        } else if (listaEnLinha.getSelectionModel().getSelectedItem().equals(caixaUser)) {
            return true;
        }

        return false;
    }

    public void engadirEnLinha(ClienteInterfaz cliente) {
        Label lbNomeUser;
        Label lbUltimaMensaxe;
        Label lbNMensaxesSenLer;
        String idUsuario = null;
        try {
            idUsuario = cliente.getIdUsuario();
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }

        lbNomeUser = new Label(idUsuario);
        lbUltimaMensaxe = new Label();
        lbNMensaxesSenLer = new Label();

        lbNomeUser.getStyleClass().add("lbNomeUser");
        lbUltimaMensaxe.getStyleClass().add("lbUltimaMensaxe");
        lbNMensaxesSenLer.getStyleClass().add("lbMensaxesSenLer");

        // Caixa onde metemos a etiqueta co nome de usuario.
        // Faise isto para que poida medrar horizontalmente
        HBox hbLbNomeUser = new HBox(lbNomeUser);
        // Caixa que se mete na posicion 0 da caixa vertical
        // Usamos o constructor co spacing e cos nodos fillos
        HBox hb = new HBox(10, hbLbNomeUser, lbNMensaxesSenLer);
        // Caixa externa que se mete na lista
        VBox caixa = new VBox(hb, lbUltimaMensaxe);

        // Editamos as propiedades das caixas
        caixa.setAlignment(Pos.CENTER_LEFT);
        hb.setAlignment(Pos.CENTER_LEFT);
        hbLbNomeUser.setAlignment(Pos.CENTER_LEFT);
        // Desta forma facemos que o nome de usuario se situe a esquerda e 
        // as mensaxes sen ler a dereita
        HBox.setHgrow(hbLbNomeUser, Priority.ALWAYS);
        // A caixa vertical ponhemoslle un padding para deixar algo de sitio
        // aos lados (orde: top, right, bottom, left)
        caixa.setPadding(new Insets(8, 10, 8, 8));
        // Axustamos o tamanho maximo da etiqueta da ultima mensaxe 
        // O padding e 8, polo que debemos restarlle algo mais desa cantidade
        lbUltimaMensaxe.setMaxWidth(listaEnLinha.getPrefWidth() - 20);

        // Ao principio a etiqueta de nmensaxes sen ler non se ve
        lbNMensaxesSenLer.setVisible(false);

        // Parte dos CHATS
        ImageView imaxe = null;
        try {
            imaxe = new ImageView("recursos/" + cliente.getFotoPerfil() + ".png");
            imaxe.setFitHeight(40);
            imaxe.setFitWidth(40);
            imaxe.setPreserveRatio(true);
        } catch (RemoteException ex) {
            Logger.getLogger(ControladorChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        lbNomeUser = new Label(idUsuario); // Creamos outra etiqueta co nome de usuario
        Label lbEnLinha = new Label("En linha");
        VBox vb2 = new VBox(1, lbNomeUser, lbEnLinha);
        hb = new HBox(15, imaxe, vb2);
        hb.setAlignment(Pos.CENTER);
        vb2.setAlignment(Pos.CENTER);
        hb.getStyleClass().add("cabeceira");
        lbNomeUser.getStyleClass().add("tituloCabeceira");
        lbEnLinha.getStyleClass().add("etqSecundaria");

        // Personalizamos a lista que sera o chat con esta persoa
        ListView<HBox> lista = new ListView<>();
        lista.setPrefSize(560, 500);
        lista.getStyleClass().add("mensaxesList");
        VBox vb = new VBox(hb, lista);
        vb.setAlignment(Pos.TOP_CENTER);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                synchronized (listaEnLinha) {
                    listaEnLinha.getItems().add(caixa);
                }
                try {
                    synchronized (listasChats) {
                        listasChats.put(cliente.getIdUsuario(), vb);
                    }
                } catch (RemoteException e) {
                    System.out.println(e.getMessage());
                }
                lbInfo.setText("Selecciona un amigo para comezar a conversar");
            }

        });

        try {
            synchronized (usuariosEnLinha) {
                usuariosEnLinha.put(cliente.getIdUsuario(), cliente);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ControladorChat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eliminarEnLinha(String idUsuarioDesconectado) {

        VBox caixa = obterCaixaUsuarioListaEnLinha(idUsuarioDesconectado);
        String id = obterCampoEnLinhaNomeUser(caixa).getText();

        // Eliminamos a referencia remota do cliente
        synchronized (usuariosEnLinha) {
            usuariosEnLinha.remove(id);
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int tam;
                synchronized (listaEnLinha) {
                    // Eliminamos a caixa do usuario nos en linha
                    tam = listaEnLinha.getItems().size();
                    listaEnLinha.getItems().remove(caixa);
                }

                synchronized (listasChats) {
                    // En caso de que se teña aberto o chat co cliente que se 
                    // acaba de marchar desactivamos o boton
                    if (xestorListas.getChildren().indexOf(listasChats.get(id)) == (tam - 1)) {
                        lbInfo.setText("Selecciona un amigo para comezar a conversar");
                        btnEnviar.setDisable(true);
                    }

                    if (tam == 1) {
                        lbInfo.setText("Non hai usuarios conectados");
                        btnEnviar.setDisable(true);
                    }

                    xestorListas.getChildren().remove(listasChats.get(id));
                    // Eliminamos a entrada no hashMap de chats
                    listasChats.remove(id);
                }
            }
        });

    }

    public void recibirMensaxe(String mns, String emisor) {
        Mensaxe m = new Mensaxe(mns, nomeUser, emisor);
        engadirAoChat(m);
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
        labelNomeUser.setText(nomeUser);
    }

    public ServidorInterfaz getServidor() {
        return servidor;
    }

    public ClienteInterfaz getCliente() {
        return cliente;
    }

    public void setServidor(ServidorInterfaz servidor) {
        this.servidor = servidor;
    }

    public void setCliente(ClienteInterfaz cliente) {
        this.cliente = cliente;
    }

    public void setContrasinal(String contrasinal) {
        this.contrasinal = contrasinal;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public int getNumFotoPerfil() {
        return numFotoPerfil;
    }

    public String getContrasinal() {
        return this.contrasinal;
    }

    /*
    
            FUNCIONS PRIVADAS PARA OBTER OBXECTOS DA INTERFAZ -- INICIO
    
     */
    private Label obterCampoEnLinhaUltimaMensaxeEnviada(VBox caixa) {
        Label lb = (Label) caixa.getChildren().get(1);

        return lb;
    }

    private Label obterCampoEnLinhaNomeUser(VBox caixa) {
        HBox hb = (HBox) ((HBox) caixa.getChildren().get(0)).getChildren().get(0);

        return (Label) hb.getChildren().get(0);
    }

    private Label obterCampoEnLinhaNMensaxesSenLer(VBox caixa) {
        HBox hb = (HBox) caixa.getChildren().get(0);

        return (Label) hb.getChildren().get(1);
    }

    private VBox obterCaixaUsuarioListaEnLinha(String usuario) {
        VBox caixa = null;

        synchronized (listaEnLinha) {
            for (int i = 0; i < listaEnLinha.getItems().size(); i++) {
                caixa = (VBox) listaEnLinha.getItems().get(i);
                if (obterCampoEnLinhaNomeUser(caixa).getText().equals(usuario)) {
                    return caixa;
                }
            }
        }

        return caixa;
    }

    private ListView<HBox> obterlistaChats(VBox caixa) {
        return (ListView<HBox>) caixa.getChildren().get(1);
    }

    /*
    
            FUNCIONS PRIVADAS PARA OBTER OBXECTOS DA INTERFAZ -- FIN
    
     */
}
