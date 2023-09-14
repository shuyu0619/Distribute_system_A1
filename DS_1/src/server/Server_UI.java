//Name: Shuyu Chen
//Student ID: 1174258

package server;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Server_UI extends Thread {

    public Server_UI() {
        SwingUtilities.invokeLater(this::initialize);
    }

    public void run() {
        // Background code if needed.
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JLabel searchResult = new JLabel("Search Result: ");
        searchButton.addActionListener(e -> {
            String word = searchField.getText().trim();
            String meaning = Server.dictionary.getOrDefault(word, "Not found");
            searchResult.setText("Search Result: " + meaning);
            Server.queryCount.put(word, Server.queryCount.getOrDefault(word, 0) + 1);
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(searchResult);
        return searchPanel;
    }

    private void initialize() {
        JFrame frame = new JFrame("Server UI");
        frame.setBounds(100, 50, 800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weighty = 1.0;  // This can push other components down
        gbc.anchor = GridBagConstraints.NORTH;  // This anchors the component to the top
        gbc.insets = new Insets(10, 0, 0, 0);

        JLabel titleLabel = new JLabel("Server_UI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        JLabel userCountLabel = new JLabel("Connected Users: 0", SwingConstants.CENTER);
        userCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        frame.add(userCountLabel, gbc);

        JPanel searchPanel = createSearchPanel();
        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.add(searchPanel, gbc);

        JLabel mostQueriedLabel = new JLabel("Top 3 Queried Words: ");
        userCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 30));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        frame.add(mostQueriedLabel, gbc);

        frame.setVisible(true);

        Timer timer = new Timer(1000, e -> {
            userCountLabel.setText("Connected Users: " + Server.connectedUsers);
            StringBuilder mostQueriedWords = new StringBuilder();
            Server.queryCount.entrySet().stream()
                    .sorted((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(3)
                    .forEach(entry -> mostQueriedWords.append(entry.getKey()).append(" (").append(entry.getValue()).append(" times), "));

            mostQueriedLabel.setText("Top 3 Queried Words: " + mostQueriedWords);
        });
        timer.start();
    }
}

