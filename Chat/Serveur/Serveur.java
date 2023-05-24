package Chat.Serveur;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Serveur extends DatagramSocket implements  Runnable {

    /**
     * Port du serveur
     */
    private static final int port = 5000;

    /**
     * Instance du serveur
     */
    private  static  Serveur isntance = null;

    /**
     * Map des clients
     */
    private volatile HashMap<String, Pair<Integer, InetAddress>> clientMap;

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
        clientMap = new HashMap<>();
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

    public void run() {
        //Creation du pool de thread
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENT);
        while (true) {
            try {
                //Reception du packet
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                receive(packet);

                //Creation du thread client
                pool.submit(new ServeurClient(packet.getAddress(), packet.getPort()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ajoute un client à la liste des clients
     * @param pseudo
     * @param port
     * @param address
     */
    public void addClient(String pseudo, int port, InetAddress address) {
        clientMap.put(pseudo, new Pair<>(port, address));
    }

    /**
     * Supprime un client de la liste des clients
     * @param pseudo
     */
    public void removeClient(String pseudo) {
        clientMap.remove(pseudo);
    }

    /**
     * Retourne si un client est présent dans la liste des clients
     * @param pseudo
     * @return
     */
    public boolean hasClient(String pseudo) {
        return clientMap.containsKey(pseudo);
    }

    /**
     * Envoie un packet à un client
     * @param packet
     * @throws Exception
     */
    public void send(String destinataire, String message, String pseudo) throws Exception {
        
        message = pseudo + " : " + message;
        
        //Si le destinataire est all, on envoie à tous les clients
        if(destinataire.equals("all")){
            for (String key : clientMap.keySet()) {
                if(key.equals(pseudo)){
                    continue;
                }
                Pair<Integer, InetAddress> pair = clientMap.get(key);
                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), pair.getSecond(), pair.getFirst());
                send(packet);
            }
            return;
        }

        //Si le destinataire n'est pas dans la liste des clients, on lance une exception        
        if (!hasClient(destinataire)) {
            throw new Exception("Le client n'existe pas");
        }

        //On envoie le packet au destinataire
        Pair<Integer, InetAddress> pair = clientMap.get(destinataire);
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), pair.getSecond(), pair.getFirst());
        send(packet);
    }

}
