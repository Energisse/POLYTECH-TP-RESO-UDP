package Serveur;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Serveur extends  DatagramSocket implements  Runnable{
    /**
     * Port du serveur
     */
    private static final int port = 9000;

    /**
     * Instance du serveur
     */
    private  static  Serveur isntance = null;

    /**
     * Constructeur du serveur
     * @throws SocketException
     */
    private Serveur() throws SocketException {
        super(port);
        System.out.println("Serveur.Serveur UDP démarré sur le port" + port);
    }

    /**
     * Retourne l'instance du serveur
     * @return
     * @throws SocketException
     */
    public static Serveur getInstance() throws SocketException {
        if(isntance == null){
            isntance = new Serveur();
        }
        return isntance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //Reception du packet
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                receive(packet);
                System.out.println("Nouveau client : @" + packet.getAddress() + ":" + packet.getPort());
                
                //Envoi du packet
                String responseMessage = "Serveur.Serveur RX302 ready";
                DatagramPacket packetResponse = new DatagramPacket(responseMessage.getBytes(), responseMessage.length(),  packet.getAddress(), packet.getPort());
                send(packetResponse);
                
                //Reception du packet
                DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
                receive(receivedPacket);
                String message = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                System.out.println("Nouveau message[" + port + "] : " + message);
        
                //Envoi du packet
                packetResponse = new DatagramPacket(message.getBytes(), message.length(), packet.getAddress(),  packet.getPort());
                send(packetResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
