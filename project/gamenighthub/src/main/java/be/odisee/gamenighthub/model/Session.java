package be.odisee.gamenighthub.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Session {
    private Game game;
    private LocalDate datum;
    private String locatie;
    private List<Player> spelers;
    private List<Score> scores;

    public Session(Game game, LocalDate datum, String locatie) {
        this.game = game;
        this.datum = datum;
        this.locatie = locatie;
        this.spelers = new ArrayList<>();
        this.scores = new ArrayList<>();
    }

    /**
     * Voegt een speler toe. Een speler die al deelneemt, wordt genegeerd.
     * @throws IllegalArgumentException als speler null is
     * @throws IllegalStateException    als de sessie volzet is
     */
    public void voegSpelerToe(Player speler) {
        if (speler == null) {
            throw new IllegalArgumentException("speler is verplicht");
        }
        if (spelers.contains(speler)) {
            return;
        }
        if (isVolzet()) {
            throw new IllegalStateException("de sessie is volzet");
        }
        spelers.add(speler);
    }

    public boolean isVolzet() {
        return spelers.size() >= game.getMaxSpelers();
    }

    /**
     * @throws IllegalArgumentException als de speler niet deelneemt of de punten negatief zijn
     * @throws IllegalStateException    als de speler al een score heeft
     */
    public void registreerUitslag(Player speler, int punten, boolean gewonnen) {
        if (!spelers.contains(speler)) {
            throw new IllegalArgumentException("speler neemt niet deel aan deze sessie");
        }
        for (Score bestaand : scores) {
            if (bestaand.getSpeler().equals(speler)) {
                throw new IllegalStateException("speler heeft al een score");
            }
        }
        scores.add(new Score(speler, punten, gewonnen));
    }

    /** Een sessie is compleet als ze spelers heeft en elke deelnemer een score heeft. */
    public boolean isCompleet() {
        return !spelers.isEmpty() && scores.size() == spelers.size();
    }

    public Game getGame() {
        return game;
    }

    public String getLocatie() {
        return locatie;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public List<Player> getSpelers() {
        return new ArrayList<>(spelers);
    }

    public List<Score> getScores() {
        return new ArrayList<>(scores);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;
        return Objects.equals(game, session.game) && Objects.equals(datum, session.datum) && Objects.equals(locatie, session.locatie);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(game);
        result = 31 * result + Objects.hashCode(datum);
        result = 31 * result + Objects.hashCode(locatie);
        return result;
    }
}
