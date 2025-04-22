package Server.DataAccessObject;

import Server.model.Player;

import java.util.List;

public interface PlayerDAO {
    Player findByUsername(String username);
    Player findById(String id);
    Player save(Player player);
    void update(Player player);
    void delete(String username);
    List<Player> findAll();
}
