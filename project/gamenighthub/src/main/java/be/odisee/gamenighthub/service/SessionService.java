package be.odisee.gamenighthub.service;

import be.odisee.gamenighthub.model.Game;
import be.odisee.gamenighthub.model.Session;
import be.odisee.gamenighthub.repository.GameRepository;
import be.odisee.gamenighthub.repository.SessionRepository;
import java.time.LocalDate;

public class SessionService {
    private final SessionRepository sessionRepository;
    private final GameRepository gameRepository;

    public SessionService(SessionRepository sessionRepository, GameRepository gameRepository) {
        this.sessionRepository = sessionRepository;
        this.gameRepository = gameRepository;
    }

    public Session plan(String gameTitel, LocalDate datum, String locatie) {
        Game game = gameRepository.zoekOpTitel(gameTitel);
        if (game == null) {
            throw new IllegalArgumentException("onbekend spel: " + gameTitel);
        }
        Session sessie = new Session(game, datum, locatie);
        sessionRepository.save(sessie);
        return sessie;
    }
}
