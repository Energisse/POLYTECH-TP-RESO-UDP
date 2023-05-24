package Chat.Client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client extends DatagramSocket implements Runnable {
    
    /**
     * Port du serveur
     */
    private static final int PORT = 5000;

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
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ADDRESS = address;
    }
    
    /**
     * Constructeur
     * @throws SocketException
     */
    public Client () throws SocketException {
        super();
    }

    @Override
    public void run() {

        String message = "Hello";
        int port;
        try {
            
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(),  ADDRESS, PORT) ;
            send(packet);

            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
            receive(response);

            port = response.getPort();
            java.net.InetAddress address = response.getAddress();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez votre Pseudo : ");

            while (true) {
                String pseudo = scanner.nextLine();
                packet = new DatagramPacket(pseudo.getBytes(), pseudo.length(), address,port) ;
                send(packet);

                response = new DatagramPacket(new byte[1024], 1024);
                receive(response);
                String responseMessage = new String(response.getData(), 0, response.getLength());
                System.out.println(responseMessage);
                if(responseMessage.equals("OK"))break;
                else System.out.println("Pseudo déjà utilisé");
            }

            ClientLecture clientLecture = new ClientLecture(this);
            ClientEcriture clientEcriture = new ClientEcriture(this, address, port, scanner);
            Thread threadLecture = new Thread(clientLecture);
            Thread threadEcriture = new Thread(clientEcriture);
            threadLecture.start();
            threadEcriture.start();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
