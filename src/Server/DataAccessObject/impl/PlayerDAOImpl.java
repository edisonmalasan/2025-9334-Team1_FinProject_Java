package Server.DataAccessObject.impl;

import Server.DataAccessObject.PlayerDAO;
import Server.model.Player;

import java.util.Collections;
import java.util.List;

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
