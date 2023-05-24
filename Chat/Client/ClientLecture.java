package Chat.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class ClientLecture implements Runnable{

    /**
     * Socket du client
     */
    DatagramSocket client;

    /**
     * Constructeur
     * @param client
     * @throws SocketException
     */
    public ClientLecture(DatagramSocket client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //Creation du packet
                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                client.receive(response);
                String responseMessage = new String(response.getData(), 0, response.getLength());
                
                //Affichage du message
                if(responseMessage.equals("NO_USER")){
                    System.out.println("Le pseudo n'existe pas");
                    continue;
                }
                else{
                    System.out.println("message de " + responseMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
