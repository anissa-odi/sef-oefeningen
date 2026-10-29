package be.odisee.gamenighthub.model;

import java.util.Locale;

public class Game {
    private String titel;
    private int minSpelers;
    private int maxSpelers;
    private int gemiddeldeSpeelduur;   // in minuten

    public Game(String titel, int minSpelers, int maxSpelers, int gemiddeldeSpeelduur) {
        if (titel == null || titel.isBlank()) {
            throw new IllegalArgumentException("titel mag niet leeg zijn");
        }
        if (minSpelers < 1) {
            throw new IllegalArgumentException("minSpelers moet minstens 1 zijn");
        }
        if (maxSpelers < minSpelers) {
            throw new IllegalArgumentException("maxSpelers mag niet kleiner zijn dan minSpelers");
        }
        if (gemiddeldeSpeelduur <= 0) {
            throw new IllegalArgumentException("gemiddeldeSpeelduur moet positief zijn");
        }
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

    /** Is dit spel geschikt voor dit aantal spelers? Grenzen inbegrepen. */
    public boolean isGeschiktVoor(int aantalSpelers) {
        return aantalSpelers >= minSpelers && aantalSpelers <= maxSpelers;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game)) {
            return false;
        }
        Game other = (Game) o;
        return this.titel.equalsIgnoreCase(other.titel);
    }

    @Override
    public int hashCode() {
        return titel.toLowerCase(Locale.ROOT).hashCode();
    }
}
