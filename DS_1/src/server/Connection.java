//Name: Shuyu Chen
//Student ID: 1174258

package server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection extends Thread {
    private BufferedReader reader;
    private BufferedWriter writer;

    public Connection(Socket socket) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


    public void run() {
        Server.incrementUserCount();  // Increment user count when a new connection starts
        String request;
        try {
            while ((request = reader.readLine()) != null) {
                System.out.println(request);
                String[] temp = request.split("#");
                if (temp.length <= 1) {
                    System.out.println("Invalid request");
                    continue;
                }
                String cmd = temp[0];
                String word = temp[1];
                String meaning = "";
                if (temp.length > 2) {
                    meaning = temp[2];
                }
                handleCommand(cmd, word, meaning);
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            Server.decrementUserCount();  // Decrement user count when a connection is closed or an exception occurs
        }
    }


    private void handleCommand(String cmd, String word, String meaning) throws IOException {

        switch (cmd) {
            case "ADD" -> {
                writer.write(Server.dictionary.containsKey(word) ? "1\n" : "0\n");
                if (!Server.dictionary.containsKey(word)) {
                    Server.dictionary.put(word, meaning);
                }
            }
            case "QUERY" -> {
                Server.queryCount.merge(word, 1, Integer::sum);
                writer.write(Server.dictionary.containsKey(word) ? "0\n" + Server.dictionary.get(word) + "\n" : "1\n");
            }
            case "DELETE" -> {
                writer.write(Server.dictionary.containsKey(word) ? "0\n" : "1\n");
                Server.dictionary.remove(word);
            }
            case "UPDATE" -> {
                writer.write(Server.dictionary.containsKey(word) ? "0\n" : "1\n");
                if (Server.dictionary.containsKey(word)) {
                    Server.dictionary.put(word, meaning);
                }
            }
            default -> writer.write("Invalid command.\n");
        }
        writer.flush();
        Server.writeDic();
        System.out.println(Server.dictionary.toString());
        System.out.println("Saved to dictionary.");
    }
}

