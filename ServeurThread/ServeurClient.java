package ServeurThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServeurClient  implements Runnable {

    /**
     * Port du client
     */
    private int port;

    /**
     * Adresse du client
     */
    private  java.net.InetAddress address;
    
    /**
     * Constructeur du client
     */
    public ServeurClient(InetAddress address, int port) throws SocketException {
        this.port = port;
        this.address = address;
    }

    @Override
    public void run() {
        //Creation du serveur UDP
        DatagramSocket server  = null;

        try {
            server = new DatagramSocket();

            System.out.println("Serveur client démarré : @" + address + ":" + port);

            //Envoi du packet
            String responseMessage = "Serveur.Serveur RX302 ready";
            DatagramPacket packetResponse = new DatagramPacket(responseMessage.getBytes(), responseMessage.length(), address, port);
            server.send(packetResponse);

            //Reception du packet
            DatagramPacket receivedPacket = new DatagramPacket(new byte[4096], 4096);
            server.receive(receivedPacket);
            
            //Affichage du message
            String message = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            System.out.println("Nouveau message[" + port + "] : " + message);
            
            //Envoi du packet
            packetResponse = new DatagramPacket(message.getBytes(), message.length(), address, port);
            server.send(packetResponse);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Fermeture du serveur
        if(server != null)  {
            server.close();
        }
        System.out.println("Serveur [" + port   + "] client fermé");
    }
}
