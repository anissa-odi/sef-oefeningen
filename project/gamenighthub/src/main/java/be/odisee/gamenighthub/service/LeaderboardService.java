package be.odisee.gamenighthub.service;

import be.odisee.gamenighthub.repository.SessionRepository;
import java.util.List;

public class LeaderboardService {
    private final SessionRepository sessionRepository;
    private final LeaderboardCalculator calculator;

    public LeaderboardService(SessionRepository sessionRepository, LeaderboardCalculator calculator) {
        this.sessionRepository = sessionRepository;
        this.calculator = calculator;
    }

    public List<LeaderboardEntry> geefLeaderboard() {
        return calculator.berekenLeaderboard(sessionRepository.findAll());
    }
}
