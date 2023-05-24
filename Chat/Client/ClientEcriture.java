package Chat.Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientEcriture implements Runnable {

    /**
     * Socket du client
     */
    private DatagramSocket client;

    /**
     * Adresse du serveur
     */
    private InetAddress address;

    /**
     * Port du serveur
     */
    private int port;

    /**
     * Scanner
     */
    private  Scanner scanner;


    /**
     * Constructeur
     * @param client
     * @param address
     * @param port
     * @param scanner
     * @throws SocketException
     */
    public ClientEcriture(DatagramSocket client, InetAddress address, int port, Scanner scanner) {
        this.client = client;
        this.address = address;
        this.port = port;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        while(true){
            System.out.print("[@destinataire:massage] : ");
            String message = scanner.nextLine();
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), address,port) ;
            try {
                client.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
