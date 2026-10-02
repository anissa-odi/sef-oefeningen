package be.odisee.gamenighthub.repository;

import be.odisee.gamenighthub.model.Player;

import java.util.List;

public interface PlayerRepository {
    void save(Player player);
    List<Player> findAll();
    Player findByBijnaam(String bijnaam);  // null als niet gevonden
    void delete(Player player);
}
