package Server.service.impl;

import Server.exception.InvalidCredentialsException;
import Server.exception.SessionExistsException;
import Server.exception.UsernameExistsException;
import Server.model.Player;
import Server.service.AuthService;

public class AuthServiceImpl implements AuthService {
    @Override
    public Player login(String username, String password) throws InvalidCredentialsException, SessionExistsException {
        return null;
    }

    @Override
    public void logout(String sessionId) {

    }

    @Override
    public Player register(Player player) throws UsernameExistsException {
        return null;
    }

    @Override
    public boolean validateSession(String sessionId) {
        return false;
    }
}
