package be.odisee.gamenighthub.model;

import java.util.Objects;

public class Score {
    private Player speler;
    private int punten;
    private boolean gewonnen;

    public Score(Player speler, int punten, boolean gewonnen) {
        if (speler == null) {
            throw new IllegalArgumentException("speler is verplicht");
        }
        if (punten < 0) {
            throw new IllegalArgumentException("punten mogen niet negatief zijn");
        }
        this.speler = speler;
        this.punten = punten;
        this.gewonnen = gewonnen;
    }

    public Player getSpeler() {
        return speler;
    }

    public int getPunten() {
        return punten;
    }

    public boolean isGewonnen() {
        return gewonnen;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;
        return punten == score.punten && gewonnen == score.gewonnen && Objects.equals(speler, score.speler);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(speler);
        result = 31 * result + punten;
        result = 31 * result + Boolean.hashCode(gewonnen);
        return result;
    }
}
