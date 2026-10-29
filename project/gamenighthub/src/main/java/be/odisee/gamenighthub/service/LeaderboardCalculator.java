package be.odisee.gamenighthub.service;

import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Score;
import be.odisee.gamenighthub.model.Session;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Berekent het leaderboard: per speler de som van de punten en het aantal overwinningen
 * over alle meegegeven sessies.
 *
 * Volgorde: meeste punten eerst, bij gelijke punten de meeste overwinningen,
 * bij gelijk aantal ook nog alfabetisch op bijnaam.
 */
public class LeaderboardCalculator {

    private static final Comparator<LeaderboardEntry> VOLGORDE = Comparator
        .comparingInt(LeaderboardEntry::getTotaalPunten).reversed()
        .thenComparing(Comparator.comparingInt(LeaderboardEntry::getAantalOverwinningen).reversed())
        .thenComparing(entry -> entry.getSpeler().getBijnaam());

    public List<LeaderboardEntry> berekenLeaderboard(List<Session> sessies) {
        Map<Player, Integer> punten = new LinkedHashMap<>();
        Map<Player, Integer> overwinningen = new LinkedHashMap<>();

        for (Session sessie : sessies) {
            for (Score score : sessie.getScores()) {
                punten.merge(score.getSpeler(), score.getPunten(), Integer::sum);
                overwinningen.merge(score.getSpeler(), score.isGewonnen() ? 1 : 0, Integer::sum);
            }
        }

        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        for (Player speler : punten.keySet()) {
            leaderboard.add(new LeaderboardEntry(speler, punten.get(speler), overwinningen.get(speler)));
        }
        leaderboard.sort(VOLGORDE);
        return leaderboard;
    }
}
