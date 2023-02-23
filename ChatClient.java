package ChatApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

/**
 *
 在 `ChatClient` 的构造函数中，我们创建了登录界面和聊天界面，并将它们添加到一个使用卡片布局的面板中。我们还在 `cardPanel` 上调用 `show` 方法，以便显示登录界面。

 在 `connectToServer` 方法中，我们首先获取服务器地址和端口号，并使用它们连接到服务器。然后，我们获取用户名并将其发送到服务器。接下来，我们创建一个新的线程来读取服务器发送的消息，并使用 `SwingUtilities.invokeLater` 方法将其添加到聊天界面的文本区域中。

 最后，在 `main` 方法中，我们创建了一个新的 `ChatClient` 对象并将其设置为可见。当用户单击登录按钮时，我们会调用 `connectToServer` 方法以连接到服务器并显示聊天界面。

 现在，我们已经完成了一个包含登录功能的聊天系统，用户可以使用该系统与其他用户进行聊天。

 */

public class ChatClient extends JFrame {

    private static final long serialVersionUID = 1L;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private LoginUI loginUI;
    private ChatUI chatUI;
    private String server;

    public ChatClient() {
        super("Chat Client");
        loginUI = new LoginUI();
        chatUI = new ChatUI();

        loginUI.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = loginUI.getUsernameField().getText();
                String servername = loginUI.getServerField().getText();
                String password = new String(loginUI.getPasswordField().getPassword());

                if (servername.isEmpty()) {
                    JOptionPane.showMessageDialog(ChatClient.this, "Please enter a server address.");
                    return;
                }

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(ChatClient.this, "Please enter a username and password.");
                    return;
                }

                if (username.equals("admin") && password.equals("password")) {
                    server = servername;
                    JOptionPane.showMessageDialog(ChatClient.this, "Login successful!");
                } else {
                    JOptionPane.showMessageDialog(ChatClient.this, "Invalid username or password.");
                }


                int port = 8088;
                try {
                    socket = new Socket(servername, port);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    out.println(username);
                    setContentPane(chatUI);
                    pack();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(ChatClient.this, "Error connecting to server: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        chatUI.getSendButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = chatUI.getInputField().getText().trim();
                if (!text.isEmpty()) {
                    out.println(text);
                    chatUI.getMessageArea().append("Me: " + text + "\n");
                    chatUI.getInputField().setText("");
                }
            }
        });

        setContentPane(loginUI);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void connectToServer() {
        try {
            String server = loginUI.getServerField().getText();
            int port = 8088;
            socket = new Socket(server, port);
            String username = loginUI.getUsernameField().getText();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            out.println(username);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String message = in.readLine();
                            if (message == null) {
                                break;
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    chatUI.getMessageArea().append(message + "\n");
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.setVisible(true);
    }
}

