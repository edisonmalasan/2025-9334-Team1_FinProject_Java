package Server.service;

import Server.exception.PlayerNotFoundException;
import Server.exception.UsernameExistsException;
import Server.model.Player;

import java.util.List;

public interface AdminService {
    List<Player> getAllPlayers();
    Player createPlayer(Player player) throws UsernameExistsException;
    Player updatePlayer(Player player) throws PlayerNotFoundException;
    void deletePlayer(String username) throws PlayerNotFoundException;
    Player searchPlayer(String username) throws PlayerNotFoundException;
    void updateGameSettings(int waitTime, int roundDuration);
}
