package Chat.Serveur;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) throws SocketException {
        Serveur.getInstance().run();
    }
}
