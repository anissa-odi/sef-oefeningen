package be.odisee.gamenighthub.repository;

import be.odisee.gamenighthub.model.Game;

import java.util.List;

public interface GameRepository {
    void save(Game game);
    List<Game> findAll();
    Game findByTitel(String titel);      // null als niet gevonden
    void delete(Game game);
}
