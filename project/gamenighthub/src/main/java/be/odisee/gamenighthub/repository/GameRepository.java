package be.odisee.gamenighthub.repository;

import be.odisee.gamenighthub.model.Game;

public interface GameRepository {
    /** @return het spel, of null als de titel onbekend is */
    Game zoekOpTitel(String titel);
}
