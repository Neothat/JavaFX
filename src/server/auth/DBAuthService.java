package server.auth;

import server.handler.SQLHandler;

public class DBAuthService implements AuthService{
    @Override
    public String getNickByLoginPass(String login, String password) {
        return SQLHandler.getNicknameByLoginAndPassword(login, password);
    }

    @Override
    public boolean changeNick(String oldNickname, String newNickname){
        return SQLHandler.changeNick(oldNickname, newNickname);
    }
}
