package be.odisee.gamenighthub.model;

import java.util.Objects;

public class Game {
    private String titel;
    private int minSpelers;
    private int maxSpelers;
    private int gemiddeldeSpeelduur;   // in minuten

    public Game(String titel, int minSpelers, int maxSpelers, int gemiddeldeSpeelduur) {
        this.titel = titel;
        this.minSpelers = minSpelers;
        this.maxSpelers = maxSpelers;
        this.gemiddeldeSpeelduur = gemiddeldeSpeelduur;
    }

    public String getTitel() {
        return titel;
    }

    public int getMinSpelers() {
        return minSpelers;
    }

    public int getMaxSpelers() {
        return maxSpelers;
    }

    public int getGemiddeldeSpeelduur() {
        return gemiddeldeSpeelduur;
    }

    public boolean isGeschiktVoor(int aantalSpelers) {
        return aantalSpelers >= minSpelers && aantalSpelers <= maxSpelers;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game)) {
            return false;
        }
        Game andere = (Game) o;
        return this.titel.equals(andere.titel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titel);
    }

    @Override
    public String toString() {
        return titel + " (" + minSpelers + "-" + maxSpelers + " spelers)";
    }
}
