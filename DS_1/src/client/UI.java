//Name: Shuyu Chen
//Student ID: 1174258

package client;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.*;

public class UI {
    private JFrame frmDictionary;
    private JTextField txtWord;
    private JTextArea txtMeaning;
    private JTextArea txtResult;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnQuery;
    Client client = new Client();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UI window = new UI();
                window.frmDictionary.setVisible(true);
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        });
    }

    public UI() {
        client.start();
        initialize();
    }

    private void initialize() {
        setUpFrame();
        addComponents();
        addActionListeners();
    }

    private void setUpFrame() {
        frmDictionary = new JFrame();
        frmDictionary.setTitle("Dictionary");
        frmDictionary.setBounds(100, 100, 576, 300);
        frmDictionary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmDictionary.getContentPane().setLayout(null);
    }

    private void addComponents() {
        JLabel lblClientUi = new JLabel("Client UI");
        lblClientUi.setBounds(156, 10, 150, 41);
        lblClientUi.setFont(new Font("Calibri", Font.BOLD, 32));
        frmDictionary.getContentPane().add(lblClientUi);

        JLabel lblInputYourWord = new JLabel("Input word here:");
        lblInputYourWord.setBounds(47, 65, 150, 16);
        frmDictionary.getContentPane().add(lblInputYourWord);

        JLabel lblInputMeaningHere = new JLabel("Input meaning here:");
        lblInputMeaningHere.setBounds(35, 90, 200, 16);
        frmDictionary.getContentPane().add(lblInputMeaningHere);

        JLabel lblKeepBlankIf = new JLabel("(keep blank if not adding)");
        lblKeepBlankIf.setBounds(10, 110, 200, 16);
        frmDictionary.getContentPane().add(lblKeepBlankIf);

        JLabel lblResult = new JLabel("Result:");
        lblResult.setBounds(74, 159, 54, 16);
        frmDictionary.getContentPane().add(lblResult);

        txtWord = new JTextField();
        txtWord.setBounds(199, 58, 245, 21);
        frmDictionary.getContentPane().add(txtWord);
        txtWord.setColumns(10);

        txtMeaning = new JTextArea();
        txtMeaning.setBounds(200, 86, 245, 39);
        frmDictionary.getContentPane().add(txtMeaning);

        txtResult = new JTextArea();
        txtResult.setEditable(false);
        txtResult.setBounds(200, 135, 245, 71);
        frmDictionary.getContentPane().add(txtResult);

        btnAdd = new JButton("ADD");
        btnAdd.setBounds(43, 228, 93, 22);
        frmDictionary.getContentPane().add(btnAdd);

        btnDelete = new JButton("DELETE");
        btnDelete.setBounds(168, 228, 93, 22);
        frmDictionary.getContentPane().add(btnDelete);

        btnQuery = new JButton("QUERY");
        btnQuery.setBounds(292, 228, 93, 22);
        frmDictionary.getContentPane().add(btnQuery);

        btnUpdate = new JButton("UPDATE");
        btnUpdate.setBounds(425, 228, 93, 22);
        frmDictionary.getContentPane().add(btnUpdate);
    }


    private void addActionListeners() {
        btnUpdate.addActionListener(arg0 -> handleUpdate());
        btnAdd.addActionListener(arg0 -> handleAdd());
        btnDelete.addActionListener(arg0 -> handleDelete());
        btnQuery.addActionListener(arg0 -> handleQuery());
    }

    private void handleUpdate() {
        try {
            String word = txtWord.getText().trim();
            String meaning = txtMeaning.getText().trim();

            if (word.isEmpty() || meaning.isEmpty()) {
                txtResult.setText("Please input both word and meaning!");
                return;
            }

            String command = "UPDATE#" + word.toLowerCase() + "#" + meaning;
            client.requestQueue.put(command);
            String resp = client.getResp();

            if (resp == null) {
                txtResult.setText("You are disconnected.");
            } else if (resp.equals("1")) {
                txtResult.setText("No such word!");
            } else if (resp.equals("0")) {
                txtResult.setText("Update successfully.");
            } else {
                txtResult.setText("Update failed.");
            }

        } catch (InterruptedException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void handleAdd() {
        try {
            String word = txtWord.getText().trim();
            String meaning = txtMeaning.getText().trim();

            if (word.isEmpty() || meaning.isEmpty()) {
                txtResult.setText("Please input both word and meaning!");
                return;
            }

            String command = "ADD#" + word.toLowerCase() + "#" + meaning;
            client.requestQueue.put(command);
            String resp = client.getResp();

            if (resp == null) {
                txtResult.setText("You are disconnected.");
            } else if (resp.equals("1")) {
                txtResult.setText("Duplicate word!");
            } else if (resp.equals("0")) {
                txtResult.setText("Added successfully.");
            } else {
                txtResult.setText("Add failed.");
            }

        } catch (InterruptedException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void handleDelete() {
        try {
            String word = txtWord.getText().trim();

            if (word.isEmpty()) {
                txtResult.setText("Please input a word!");
                return;
            }

            String command = "DELETE#" + word.toLowerCase();
            client.requestQueue.put(command);
            String resp = client.getResp();

            if (resp == null) {
                txtResult.setText("You are disconnected.");
            } else if (resp.equals("1")) {
                txtResult.setText("No such word!");
            } else if (resp.equals("0")) {
                txtResult.setText("Deleted successfully.");
            } else {
                txtResult.setText("Delete failed.");
            }

        } catch (InterruptedException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void handleQuery() {
        try {
            String word = txtWord.getText().trim();

            if (word.isEmpty()) {
                txtResult.setText("Please input a word!");
                return;
            }

            String command = "QUERY#" + word.toLowerCase();
            client.requestQueue.put(command);
            String resp = client.getResp();

            if (resp == null) {
                txtResult.setText("You are disconnected.");
            } else if (resp.equals("1")) {
                txtResult.setText("Word not found!");
            } else if (resp.equals("0")) {
                String actualMeaning = client.getResp();
                txtResult.setText("Meaning: " + actualMeaning);
            } else {
                txtResult.setText("Unexpected response: " + resp);
            }

        } catch (InterruptedException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}

