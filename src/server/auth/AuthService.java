package server.auth;

import java.sql.SQLException;

public interface AuthService {

    default void start() {}
    default void stop() {}

    String getNickByLoginPass(String login, String password);
    boolean changeNick(String oldNickname, String newNickname);

}
