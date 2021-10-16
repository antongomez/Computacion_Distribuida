package chatgrupal;

import gui.controlador.VentaChatControlador;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import javafx.application.Platform;
import mensaxes.Mensaxe;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Listener extends Thread {

    // Tamaño maximo de recpecion de mensaxes
    protected static final int MAX_SIZE = 32768;

    private final VentaChatControlador controlador;
    private final MulticastSocket socket;
    private InetSocketAddress group;

    public Listener(VentaChatControlador controlador, MulticastSocket socket) {
        this.controlador = controlador;
        this.socket = socket;
    }

    @Override
    public void run() {

        while (true) {
            try {
                // Esperamos ata recibir unha mensaxe
                byte[] buf = new byte[MAX_SIZE];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                socket.receive(recv);

                // Extraemos a mensaxe
                byte[] dato2 = recv.getData();
                String cadea = new String(dato2);
                // Creamos a mensaxe
                Mensaxe msx = lerMensaxe(cadea);

                // Actualizamos a UI dende o fio principal de JavaFx
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controlador.engadirAoChat(msx);
                    }
                });
            } catch (IOException ex) {
                if(!(ex instanceof SocketException)){
                    java.util.logging.Logger.getLogger(Listener.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }

    }

    private Mensaxe lerMensaxe(String cadea) {
        Mensaxe msx = new Mensaxe();

        String[] aux = cadea.split(":", 2);
        if (aux.length < 2) {
            // Asumimos que non ten usuario. Recibimos a mensaxe dun anonimo
            msx.setMsx(cadea);
        } else {
            // Caso normal, a mensaxe ten usuario
            msx.setNomeUser(aux[0]);
            msx.setMsx(aux[1]);
        }

        return msx;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

}
