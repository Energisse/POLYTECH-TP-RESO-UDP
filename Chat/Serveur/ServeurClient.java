package Chat.Serveur;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

public class ServeurClient implements Runnable {

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

        String pseudo = "";

        try {
            server = new DatagramSocket();
            System.out.println("Serveur client démarré : @" + address + ":" + port);

            server.setSoTimeout(30000);

            //Envoi du packet
            String message = "Serveur.Serveur RX302 ready";
            DatagramPacket packetResponse = new DatagramPacket(message.getBytes(),message.length(), address, port);
            server.send(packetResponse);


            /* Demande du pseudo */
            while (true) {
                //Reception du packet
                DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
                server.receive(receivedPacket);
                pseudo = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                System.out.println("Nouveau message[" + port + "] : " + pseudo);
                
                //Si le pseudo n'est pas déjà utilisé et n'est pas "all" on accepte
                if (!Serveur.getInstance().hasClient(pseudo) && !pseudo.equals("all")) {
                    message = "OK";
                    packetResponse = new DatagramPacket(message.getBytes(), message.length(), address, port);
                    server.send(packetResponse);
                    break;
                } else {
                    message = "PAS OK";
                    packetResponse = new DatagramPacket(message.getBytes(), message.length(), address, port);
                    server.send(packetResponse);
                }
            }
            
            //Ajout du client
            Serveur.getInstance().addClient(pseudo, port,address);

            /* Messages */
            while(true){
                //Reception du packet message
                DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
                server.receive(receivedPacket);

                message = new String(receivedPacket.getData(), 0, receivedPacket.getLength());


                String[] split =  message.split(":") ;
                
                try {
                    Serveur.getInstance().send(split[0],split[1],pseudo);
                }
                catch (Exception e){
                    message = "NO_USER";
                    packetResponse = new DatagramPacket(message.getBytes(), message.length(), address, port);
                    server.send(packetResponse);
                }

                System.out.println("Nouveau message[" + port + "] : " + message);
            }

        }catch (SocketTimeoutException e){
            System.out.println("TIMEOUT");
        }
        catch (IOException e) {
                e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(server != null)  server.close();
        System.out.println("Serveur [" + port   + "] client fermé");

        try {
            Serveur.getInstance().removeClient(pseudo);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
