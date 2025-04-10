package Server.DataAccessObject.impl;

import Server.DataAccessObject.PlayerDAO;
import Server.model.Player;

import java.util.Collections;
import java.util.List;

// TODO: Implement PlayerDAO methods to interact with the 'players' table using JDBC, including querying by username and mapping results to Player model objects.
public class PlayerDAOImpl implements PlayerDAO {

    @Override
    public Player findByUsername(String username) {
        return null;
    }

    @Override
    public Player findById(String id) {
        return null;
    }

    @Override
    public Player save(Player player) {
        return null;
    }

    @Override
    public void update(Player player) {

    }

    @Override
    public void delete(String username) {

    }

    @Override
    public List<Player> findAll() {
        return Collections.emptyList();
    }
}
