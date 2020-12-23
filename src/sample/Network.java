package sample;

import javafx.application.Platform;
import sample.controllers.Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    private static final String AUTH_OK_CMD = "/authok";
    private static final String PRIVATE_MSG_CMD = "/w";
    private static final String CLIENT_MSG_CMD_PREFIX = "/clientMsg";

    private final String host;
    private final int port;
    private ClientChat clientChat;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputSteam;
    private String nickname;

    public Network(){
        this(SERVER_ADDRESS, SERVER_PORT);
    }

    public Network(String host, int port){
        this.host = host;
        this.port = port;
    }

    public Network(ClientChat clientChat) {
        this();
        this.clientChat = clientChat;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            inputStream = new DataInputStream(socket.getInputStream());
            outputSteam = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Соединение не было установленно!");
            e.printStackTrace();
            return false;
        }
    }

    /*public DataInputStream getInputStream() {
        return inputStream;
    }*/

    public DataOutputStream getOutputSteam() {
        return outputSteam;
    }

    public void sendPrivateMessage(String user, String message) throws IOException {
        String command = String.format("%s %s %s", PRIVATE_MSG_CMD, user, message);
        sendMessage(command);
    }

    public void sendMessage(String message) throws IOException {
        getOutputSteam().writeUTF(message);
    }

    public void waitMessages(Controller controller) {
        Thread thread = new Thread(() -> {
            try {
                while (true){
                    String message = inputStream.readUTF();
                    if(clientChat.getState() == ClientChatState.AUTHENTICATION) {
                        if (message.startsWith(AUTH_OK_CMD)) {
                            String[] parts = message.split(" ", 2);
                            nickname = parts[1];
                            Platform.runLater(() -> {
                                clientChat.activeChatDialog(nickname);
                            });
                        } else {
                            Platform.runLater(() -> {
                                ClientChat.showNetworkError(message, "Auth error");
                            });
                        }
                    }else if (message.startsWith(CLIENT_MSG_CMD_PREFIX)){
                        String[] parts = message.split("\\s+", 3);
                        String sender = parts[1];
                        String msgBody = parts[2];
                        Platform.runLater(() -> {
                            controller.appendMessage(String.format("%s %s", sender, msgBody));
                        });
                    } else {
                        Platform.runLater(() -> {
                            controller.appendMessage(message);
                        });
                    }
                }
            } catch (IOException e) {
                System.err.println("Соединение было разорвано!");
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
