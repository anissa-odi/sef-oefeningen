package be.odisee.gamenighthub.service;

import be.odisee.gamenighthub.model.Player;

public class LeaderboardEntry {
    private final Player speler;
    private final int totaalPunten;
    private final int aantalOverwinningen;

    public LeaderboardEntry(Player speler, int totaalPunten, int aantalOverwinningen) {
        this.speler = speler;
        this.totaalPunten = totaalPunten;
        this.aantalOverwinningen = aantalOverwinningen;
    }

    public Player getSpeler() {
        return speler;
    }

    public int getTotaalPunten() {
        return totaalPunten;
    }

    public int getAantalOverwinningen() {
        return aantalOverwinningen;
    }
}
