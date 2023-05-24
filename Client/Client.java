import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client extends DatagramSocket implements Runnable{
    
    /**
     * Port du serveur
     */
    private static final int PORT = 9000;

    /**
     * Adresse du serveur
     */
    private static final InetAddress ADDRESS;

    /**
     * Adresse du serveur
     */
    static{
        InetAddress address = null;
        try {
            address = InetAddress.getByName("localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ADDRESS = address;
    }

    public Client () throws Exception {
        super();
    }
    
    @Override
    public void run() {
        try {
            //Creation du packet
            String message = "hello serveur RX302";
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), ADDRESS, PORT);

            //Envoi du packet
            send(packet);

            //Reception du packet
            DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
            receive(receivedPacket);

            String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            System.out.println(receivedMessage + " : @" +  receivedPacket.getAddress() + ":" + receivedPacket.getPort());


            Scanner scanner = new Scanner(System.in);
            //Entrée clavier
            System.out.print("Entrez un message : ");
            message = scanner.nextLine();

            //Creation du packet
            packet = new DatagramPacket(message.getBytes(), message.length(), receivedPacket.getAddress(), receivedPacket.getPort());

            //Envoi du packet
            send(packet);

            //Reception du packet
            receivedPacket = new DatagramPacket(new byte[1024], 1024);
            receive(receivedPacket);

            receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            System.out.println("message reçu : " + receivedMessage);

            close();

        } catch (Exception ex) {
            System.out.println("Erreur");
            System.exit(0);
        }
    }
}
