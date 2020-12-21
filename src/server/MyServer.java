package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyServer {

    private final List<ClientHandler> clients = new ArrayList<>();

    public void start(int port){

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер успешно запущен");
            runServerMessageThread();
            
            //noinspection InfiniteLoopStatement
            while (true) {
                waitAndProcessNewClientConnection(serverSocket);
            }
        } catch (IOException e) {
            System.err.println("Ошибка подключения новыхх клиентов");
            e.printStackTrace();
        }
    }

    private void waitAndProcessNewClientConnection(ServerSocket serverSocket) throws IOException {
        System.out.println("Ожидание нового пользователя...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Клиент подключился");
        processClientConnection(clientSocket);
    }

    private void processClientConnection(Socket clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.handle();
    }

    private void runServerMessageThread() {
        Thread serverMessageThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true){
                String serverMessage = scanner.next();
                try {
                    broadcastMessage(serverMessage, null);
                } catch (IOException e) {
                    System.err.println("Прерван процесс server message");
                    e.printStackTrace();
                }
            }
        });
        serverMessageThread.setDaemon(true);
        serverMessageThread.start();
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            client.sendMessage(message);
        }
    }

    public synchronized void subscribe(ClientHandler handler) {
        clients.add(handler);
    }

    public synchronized void unsubscribe(ClientHandler handler) {
        clients.remove(handler);
    }
}