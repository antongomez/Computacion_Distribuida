package servidor;

import alertas.Alerta;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import gui.controlador.ControladorAlertas;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensaxe.Mensaxe;
import mensaxe.TipoMensaxe;

/**
 *
 * @author User
 */
public class Recibidor extends Thread {

    private static final String COLA_SERVIDOR = "servidor";
    private final ConnectionFactory factory;
    private final ArrayList<Alerta> alertas;
    private Connection conexion;
    private Channel canal;

    public Recibidor(ConnectionFactory factory) {
        this.factory = factory;
        this.alertas = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            conexion = factory.newConnection();
            canal = conexion.createChannel();
            canal.queueDeclare(COLA_SERVIDOR, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                ByteArrayInputStream bs = new ByteArrayInputStream(delivery.getBody());
                try (ObjectInputStream is = new ObjectInputStream(bs)) {
                    try {
                        Mensaxe m = (Mensaxe) is.readObject();
                        System.out.println(m);
                        interpretarMensaxe(m);
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            canal.basicConsume(COLA_SERVIDOR, true, deliverCallback, consumerTag -> {
            });

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void interpretarMensaxe(Mensaxe m) {
        switch (m.getTipo()) {
            case SAUDO -> {
                // Enviamos un saudo ao servidor para que nos asigne un canal
                Mensaxe men = new Mensaxe(TipoMensaxe.SAUDO, Servidor.obterNomesEmpresas());
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                try {
                    // Declaramos a cola do servidor
                    canal.queueDeclare(m.getInfo(), false, false, false, null);
                    try (ObjectOutputStream os = new ObjectOutputStream(bs)) {
                        os.writeObject(men);
                    }
                    canal.basicPublish("", m.getInfo(), null, bs.toByteArray());
                } catch (IOException ex) {
                    Logger.getLogger(ControladorAlertas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case PONHER_ALERTA -> {
                // Evitamos que os fios do servidor interfiran un no outro
                synchronized (alertas) {
                    alertas.add(m.getAlerta());
                }
            }
            case DESPEDIDA -> {
                // Evitamos que os fios do servidor interfiran un no outro
                synchronized (alertas) {
                    // Eliminamos as alertas do cliente que se acaba de despedir
                    ArrayList<Alerta> als = new ArrayList<>(alertas);
                    for (Alerta a : als) {
                        if (a.getNomeCola().equals(m.getInfo())) {
                            alertas.remove(a);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Alerta> getAlertas() {
        return alertas;
    }

    public void eliminarAlerta(Alerta a) {
        this.alertas.remove(a);
    }

}
