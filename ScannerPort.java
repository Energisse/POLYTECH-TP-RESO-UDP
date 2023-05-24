import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class ScannerPort {

    public static void main(String[] args) {
        scanPorts(900,1000).forEach((k,v)->{
            System.out.println(k + " " + v);
        });
    }

    /**
     * Scanne les ports entre startPort et endPort
     * @param startPort Port de début
     * @param endPort Port de fin
     * @return Map<Integer,Boolean> le statut des ports
     */
    static Map<Integer,Boolean> scanPorts(int startPort, int endPort) {
        Map<Integer,Boolean> openPorts = new HashMap();
        for (int port = startPort; port <= endPort; port++) {
            try {
                DatagramSocket socket = new DatagramSocket( port);
                socket.close();
                openPorts.put(port,true);
            } catch (Exception ex) {
                openPorts.put(port,false);
            }
        }
        return openPorts;
    }
}
