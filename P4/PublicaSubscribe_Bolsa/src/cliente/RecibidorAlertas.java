package cliente;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import gui.controlador.ControladorAlertas;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import mensaxe.Mensaxe;
import servidor.Servidor;

/**
 *
 * @author User
 */
public class RecibidorAlertas extends Thread {

    private Connection conexion;
    private Channel canal;
    private final String nomeCola;
    private final ControladorAlertas controlador;

    public RecibidorAlertas(Connection conexion, Channel canal, String nomeCola, ControladorAlertas controlador) {
        this.conexion = conexion;
        this.canal = canal;
        this.nomeCola = nomeCola;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try {
            canal.queueDeclare(nomeCola, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                // Lemos a mensaxe
                ByteArrayInputStream bs = new ByteArrayInputStream(delivery.getBody());
                try (ObjectInputStream is = new ObjectInputStream(bs)) {
                    try {
                        Mensaxe m = (Mensaxe) is.readObject();
                        // Actualizamos a UI dende o fio principal de JavaFx
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (m.getAlerta() != null) {
                                    controlador.engadirAlerta(m.getAlerta());
                                } else {
                                    System.out.println("A alerta e nula");
                                }
                            }
                        });
                    } catch (IOException | ClassNotFoundException ex) {
                        System.out.println("Erro lendo a mensaxe");
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            canal.basicConsume(nomeCola, true, deliverCallback, consumerTag -> {
            });

        } catch (IOException ex) {
            System.out.println("Erro declarando a cola do cliente");
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
