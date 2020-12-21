package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private static final String END_CMD = "/end";

    private final MyServer myServer;
    private final Socket clientSocket;

    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public void handle() throws IOException {
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());

        new Thread(() -> {
        try {
            readMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (IOException e){
                System.err.println("Ошибка при закрытии соединения");
            }
        }
        }).start();
        myServer.subscribe(this);
    }

    private void readMessage() throws IOException {
        while (true){
            String message = in.readUTF();
            System.out.println("message: " + message);
            if (message.startsWith(END_CMD)) {
                return;
            } else {
                myServer.broadcastMessage(message, this);
            }
        }
    }

    private void closeConnection() throws IOException {
        myServer.unsubscribe(this);
        clientSocket.close();
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }
}
