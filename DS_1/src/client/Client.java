//Name: Shuyu Chen
//Student ID: 1174258

package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Thread {
    public static String address = "localhost";
    public static int port = 7777;
    private Socket client;
    private BufferedReader reader;
    private BufferedWriter writer;
    public final LinkedBlockingQueue<String> requestQueue = new LinkedBlockingQueue<>(1); // Marked as final
    public static final String TERMINATION_STRING = "END";  // Sentinel string

    public void run() {
        try {
            // Initialize client socket and I/O streams
            client = new Socket(address, port);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
            System.out.println("Connected");

            // Loop to handle client requests
            String reqString;
            while (true) {
                reqString = requestQueue.take();
                if (TERMINATION_STRING.equals(reqString)) {
                    break;
                }
                writer.write(reqString + "\n");
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("Disconnected due to: " + e.getMessage());
        }
    }

    public String getResp() {
        String resp = null;
        try {
            resp = reader.readLine();
        } catch (Exception e) {
            System.out.println("Disconnected due to: " + e.getMessage());
        }
        return resp;
    }
}
