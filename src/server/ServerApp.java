package server;

import server.handler.SQLHandler;

public class ServerApp {
    public static final int DEFAULT_PORT = 8189;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length != 0) {
            port = Integer.parseInt(args[0]);
        }
        if (SQLHandler.connect()){
            new MyServer().start(port);
        } else {
            throw new RuntimeException("Не удалось подключиться к БД");
        }

    }
}
