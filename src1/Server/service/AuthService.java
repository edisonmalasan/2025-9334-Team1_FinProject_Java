package Server.service;

import Server.exception.InvalidCredentialsException;
import Server.exception.SessionExistsException;
import Server.exception.UsernameExistsException;
import Server.model.Player;

public interface AuthService {
    Player login(String username, String password) throws InvalidCredentialsException, SessionExistsException;
    void logout(String sessionId);
    Player register(Player player) throws UsernameExistsException;
    boolean validateSession(String sessionId);
}
