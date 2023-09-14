//Name: Shuyu Chen
//Student ID: 1174258

package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map.Entry;

public class Server extends Thread {
    private static int port = 7777;
    public static ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Integer> queryCount = new ConcurrentHashMap<>();


    private static String dir = "src/dictionary.txt";
    public static int connectedUsers = 0;

    public static void main(String[] args) {
        if (args.length == 2) {
            dir = args[1];
            port = Integer.parseInt(args[0]);
        } else {
            System.out.println("Input is invalid, starting with default values.");
        }
        Server serverInstance = new Server();
        serverInstance.readDic();
        serverInstance.start();

        Server_UI serverUI = new Server_UI();
        serverUI.start();
    }

    @Override
    public void run() {
        try (ServerSocket ss = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket clientSocket = ss.accept();
                    Connection clientConnection = new Connection(clientSocket);
                    clientConnection.start();
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

public void readDic() {
    File readDic = new File(dir);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(readDic), StandardCharsets.UTF_8))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] lines = line.split("\t");
            if(lines.length >= 2) {
                dictionary.put(lines[0], lines[1]);
            }
        }
        System.out.println("Successfully read dictionary.");
    } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
    }
}

    public static void writeDic() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dir))) {
            for (Entry<String, String> ent : dictionary.entrySet()) {
                bw.write(ent.getKey() + "\t" + ent.getValue() + "\n");
            }
            bw.flush();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public synchronized static void incrementUserCount() {
        connectedUsers++;
        // Optionally, you can log this information
        System.out.println("Connected Users: " + connectedUsers);
    }

    public synchronized static void decrementUserCount() {
        connectedUsers--;
        // Optionally, you can log this information
        System.out.println("Connected Users: " + connectedUsers);
    }
}


