package chatgrupal;

import static chatgrupal.ChatGrupal.INET;
import static chatgrupal.ChatGrupal.PORTO;
import gui.controlador.VentaChatControlador;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensaxes.Mensaxe;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Enviador extends Thread {

    private static final int TTL = 50;

    private final MulticastSocket socket;
    private final String mensaxe;
    private final String nomeUsuario;
    private final InetSocketAddress group;

    public Enviador(MulticastSocket socket, String mensaxe, String nomeUsuario, InetSocketAddress group) {
        this.socket = socket;
        this.mensaxe = mensaxe;
        this.nomeUsuario = nomeUsuario;
        this.group = group;
    }

    @Override
    public void run() {
        Mensaxe msx = new Mensaxe();
        msx.setMsx(mensaxe);

        try {
            socket.setTimeToLive(TTL);
            String cadea = nomeUsuario + ":" + msx.getMsx();
            byte[] dato = cadea.getBytes();
            
            // Enviamos a mensaxe 
            DatagramPacket dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName(INET), PORTO);

            /* No caso de que dato.lenght sexa maior que Listener.MAX_SIZE, 
                 * a mensaxe recibirase cortada. */
            socket.send(dgp);
            // Saimos do grupo
            //socket.leaveGroup(group, NetworkInterface.getByInetAddress(InetAddress.getByName(INET)));

        } catch (IOException ex) {
            Logger.getLogger(VentaChatControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
