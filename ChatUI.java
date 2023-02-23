package ChatApp;

import javax.swing.*;
import java.awt.*;


public class ChatUI extends JPanel {
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton sendButton;


    public JButton getSendButton() {
        return sendButton;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public JTextField getInputField() {
        return inputField;
    }

    public ChatUI() {
        setLayout(new BorderLayout());

        // Message area
        messageArea = new JTextArea(10, 20);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // Input area and button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }
}



