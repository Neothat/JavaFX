package sample;

import javafx.application.Platform;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    private final String host;
    private final int port;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputSteam;

    public Network(){
        this(SERVER_ADDRESS, SERVER_PORT);
    }

    public Network(String host, int port){
        this.host = host;
        this.port = port;
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

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputSteam() {
        return outputSteam;
    }

    public void sendMessage(String message) throws IOException {
        getOutputSteam().writeUTF(message);
    }

    public void waitMessages(Controller controller) {
        Thread thread = new Thread(() -> {
            try {
                while (true){
                    String message = inputStream.readUTF();
                    Platform.runLater(() -> {
                        controller.appendMessage("Сервер: " + message);
                    });
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
