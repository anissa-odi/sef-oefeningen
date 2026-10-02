package be.odisee.gamenighthub.model;

public class Score {
    private Player speler;
    private int punten;
    private boolean gewonnen;

    public Score(Player speler, int punten, boolean gewonnen) {
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
    public String toString() {
        return speler.getBijnaam() + ": " + punten + " punten" + (gewonnen ? " (winnaar)" : "");
    }
}
