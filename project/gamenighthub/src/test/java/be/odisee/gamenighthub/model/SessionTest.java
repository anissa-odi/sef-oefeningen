package be.odisee.gamenighthub.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class SessionTest {
    private static final LocalDate DATUM = LocalDate.of(2026, 11, 14);

    private Game catan;          // 3 tot 4 spelers
    private Session sessie;
    private Player emma;
    private Player noah;
    private Player lotte;

    @BeforeEach
    void setUp() {
        catan = new Game("Catan", 3, 4, 90);
        sessie = new Session(catan, DATUM, "Gent");
        emma = new Player("Emma", new User("Emma", "emma@example.com", "hash1"));
        noah = new Player("Noah", new User("Noah", "noah@example.com", "hash2"));
        lotte = new Player("Lotte", new User("Lotte", "lotte@example.com", "hash3"));
    }

    // ===== Deel 1: bestaand gedrag =====

    @Test
    void constructor_nieuweSessie_heeftGeenDeelnemersEnGeenScores() {
        assertAll(
            () -> assertTrue(sessie.getSpelers().isEmpty()),
            () -> assertTrue(sessie.getScores().isEmpty()));
    }

    @Test
    void voegSpelerToe_nieuweSpeler_komtInDeelnemers() {
        sessie.voegSpelerToe(emma);

        assertEquals(List.of(emma), sessie.getSpelers());
    }

    @Test
    void voegSpelerToe_tweeVerschillendeSpelers_komenBeideInDeelnemers() {
        sessie.voegSpelerToe(emma);
        sessie.voegSpelerToe(noah);

        assertEquals(List.of(emma, noah), sessie.getSpelers());
    }

    @Test
    void voegSpelerToe_zelfdeSpelerTweeKeer_staatSlechtsEenKeerInLijst() {
        sessie.voegSpelerToe(emma);
        sessie.voegSpelerToe(emma);

        assertEquals(1, sessie.getSpelers().size());
    }

    @Test
    void registreerUitslag_deelnemer_bewaartScore() {
        sessie.voegSpelerToe(emma);

        sessie.registreerUitslag(emma, 12, true);

        Score score = sessie.getScores().get(0);
        assertAll(
            () -> assertEquals(1, sessie.getScores().size()),
            () -> assertEquals(emma, score.getSpeler()),
            () -> assertEquals(12, score.getPunten()),
            () -> assertTrue(score.isGewonnen()));
    }

    @Test
    void registreerUitslag_meerdereDeelnemers_bewaartScoreVoorElk() {
        sessie.voegSpelerToe(emma);
        sessie.voegSpelerToe(noah);

        sessie.registreerUitslag(emma, 12, true);
        sessie.registreerUitslag(noah, 7, false);

        assertEquals(2, sessie.getScores().size());
    }

    @ParameterizedTest
    @MethodSource("sessiesMetVerschil")
    void equals_sessieMetAnderGameLocatieOfDatum_geeftFalse(Session andere) {
        Session basis = new Session(new Game("Catan", 3, 4, 90), DATUM, "Gent");

        assertFalse(basis.equals(andere));
    }

    static Stream<Arguments> sessiesMetVerschil() {
        return Stream.of(
            Arguments.of(new Session(new Game("Risk", 2, 6, 120), DATUM, "Gent")),
            Arguments.of(new Session(new Game("Catan", 3, 4, 90), DATUM, "Brussel")),
            Arguments.of(new Session(new Game("Catan", 3, 4, 90), DATUM.plusDays(1), "Gent")));
    }

    @Test
    void equalsEnHashCode_zelfdeGameLocatieEnDatum_zijnGelijk() {
        Session a = new Session(new Game("Catan", 3, 4, 90), DATUM, "Gent");
        Session b = new Session(new Game("Catan", 2, 5, 30), DATUM, "Gent"); // andere game-details, zelfde titel

        assertAll(
            () -> assertEquals(a, b),
            () -> assertEquals(a.hashCode(), b.hashCode()));
    }

    /** Ontdekking (rood in het begin-project): de sessie geeft haar interne lijst prijs. */
    // ===== Deel 2: TDD =====

    @Test
    void getSpelers_teruggegevenLijstWijzigen_laatSessieOngewijzigd() {
        sessie.voegSpelerToe(emma);

        List<Player> lijst = sessie.getSpelers();
        lijst.clear();

        assertEquals(1, sessie.getSpelers().size());
    }

    @Test
    void getScores_teruggegevenLijstWijzigen_laatSessieOngewijzigd() {
        sessie.voegSpelerToe(emma);
        sessie.registreerUitslag(emma, 10, true);

        List<Score> lijst = sessie.getScores();
        lijst.clear();

        assertEquals(1, sessie.getScores().size());
    }

    @Test
    void isVolzet_nieuweSessie_geeftFalse() {
        assertFalse(sessie.isVolzet());
    }

    @Test
    void isVolzet_minderDanMaximumSpelers_geeftFalse() {
        sessie.voegSpelerToe(emma);
        sessie.voegSpelerToe(noah);
        sessie.voegSpelerToe(lotte); // 3 van max 4

        assertFalse(sessie.isVolzet());
    }

    @Test
    void isVolzet_maximumSpelersBereikt_geeftTrue() {
        voegVierSpelersToe();

        assertTrue(sessie.isVolzet());
    }

    @Test
    void voegSpelerToe_sessieVolzet_gooitException() {
        voegVierSpelersToe();
        Player extra = new Player("Extra", new User("Extra", "extra@example.com", "h"));

        assertThrows(IllegalStateException.class, () -> sessie.voegSpelerToe(extra));
    }

    @Test
    void voegSpelerToe_sessieVolzetDeelnemerOpnieuw_blijftStil() {
        voegVierSpelersToe();

        sessie.voegSpelerToe(emma); // al deelnemer: geen fout, geen duplicaat

        assertEquals(4, sessie.getSpelers().size());
    }

    @Test
    void voegSpelerToe_spelerNull_gooitException() {
        assertThrows(IllegalArgumentException.class, () -> sessie.voegSpelerToe(null));
    }

    @Test
    void registreerUitslag_spelerNeemtNietDeel_gooitException() {
        sessie.voegSpelerToe(emma);

        assertThrows(IllegalArgumentException.class, () -> sessie.registreerUitslag(noah, 5, false));
        assertTrue(sessie.getScores().isEmpty());
    }

    @Test
    void registreerUitslag_tweedeKeerVoorZelfdeSpeler_gooitException() {
        sessie.voegSpelerToe(emma);
        sessie.registreerUitslag(emma, 10, true);

        assertThrows(IllegalStateException.class, () -> sessie.registreerUitslag(emma, 3, false));
        assertEquals(1, sessie.getScores().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void registreerUitslag_negatievePunten_gooitException(int punten) {
        sessie.voegSpelerToe(emma);

        assertThrows(IllegalArgumentException.class, () -> sessie.registreerUitslag(emma, punten, false));
    }

    @Test
    void registreerUitslag_nulPunten_isGeldig() {
        sessie.voegSpelerToe(emma);

        sessie.registreerUitslag(emma, 0, false);

        assertEquals(0, sessie.getScores().get(0).getPunten());
    }

    @Test
    void isCompleet_geenDeelnemers_geeftFalse() {
        assertFalse(sessie.isCompleet());
    }

    @Test
    void isCompleet_nietIedereenHeeftScore_geeftFalse() {
        sessie.voegSpelerToe(emma);
        sessie.voegSpelerToe(noah);
        sessie.registreerUitslag(emma, 10, true);

        assertFalse(sessie.isCompleet());
    }

    @Test
    void isCompleet_iedereenHeeftScore_geeftTrue() {
        sessie.voegSpelerToe(emma);
        sessie.voegSpelerToe(noah);
        sessie.registreerUitslag(emma, 10, true);
        sessie.registreerUitslag(noah, 4, false);

        assertTrue(sessie.isCompleet());
    }

    private void voegVierSpelersToe() {
        Player vierde = new Player("Vier", new User("Vier", "vier@example.com", "h"));
        sessie.voegSpelerToe(emma);
        sessie.voegSpelerToe(noah);
        sessie.voegSpelerToe(lotte);
        sessie.voegSpelerToe(vierde);
    }
}
