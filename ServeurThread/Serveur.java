package ServeurThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
     * Nombre max de client
     */
    private static final int MAX_CLIENT = 10; 

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
        //Creation du pool de thread
        ExecutorService pool =  Executors.newFixedThreadPool(MAX_CLIENT);

        while (true) {
            try {
                //Creation du packet
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                receive(packet);
                System.out.println("Nouveau client : @" + packet.getAddress() + ":" + packet.getPort());

                //creation du thread client
                pool.submit(new ServeurClient(packet.getAddress(), packet.getPort()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
