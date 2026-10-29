package be.odisee.gamenighthub.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.odisee.gamenighthub.model.Game;
import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Session;
import be.odisee.gamenighthub.model.User;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaderboardCalculatorTest {
    private LeaderboardCalculator calculator;
    private Player emma;
    private Player noah;
    private Player lotte;

    @BeforeEach
    void setUp() {
        calculator = new LeaderboardCalculator();
        emma = speler("Emma");
        noah = speler("Noah");
        lotte = speler("Lotte");
    }

    @Test
    void berekenLeaderboard_geenSessies_geeftLeegLeaderboard() {
        assertTrue(calculator.berekenLeaderboard(List.of()).isEmpty());
    }

    @Test
    void berekenLeaderboard_sessieZonderScores_geeftLeegLeaderboard() {
        Session sessie = nieuweSessie();
        sessie.voegSpelerToe(emma);

        assertTrue(calculator.berekenLeaderboard(List.of(sessie)).isEmpty());
    }

    @Test
    void berekenLeaderboard_eenSessie_geeftPuntenEnOverwinningenPerSpeler() {
        Session sessie = sessieMet(new Player[] {emma, noah}, new int[] {12, 7}, new boolean[] {true, false});

        List<LeaderboardEntry> lijst = calculator.berekenLeaderboard(List.of(sessie));

        assertAll(
            () -> assertEquals(2, lijst.size()),
            () -> assertEquals(emma, lijst.get(0).getSpeler()),
            () -> assertEquals(12, lijst.get(0).getTotaalPunten()),
            () -> assertEquals(1, lijst.get(0).getAantalOverwinningen()),
            () -> assertEquals(noah, lijst.get(1).getSpeler()),
            () -> assertEquals(7, lijst.get(1).getTotaalPunten()),
            () -> assertEquals(0, lijst.get(1).getAantalOverwinningen()));
    }

    @Test
    void berekenLeaderboard_meerdereSessies_somtPuntenEnOverwinningenPerSpeler() {
        Session s1 = sessieMet(new Player[] {emma, noah}, new int[] {10, 5}, new boolean[] {true, false});
        Session s2 = sessieMet(new Player[] {emma, noah}, new int[] {3, 8}, new boolean[] {false, true});

        List<LeaderboardEntry> lijst = calculator.berekenLeaderboard(List.of(s1, s2));

        assertAll(
            () -> assertEquals(13, lijst.get(0).getTotaalPunten()),   // beide spelers hebben 13
            () -> assertEquals(13, lijst.get(1).getTotaalPunten()),
            () -> assertEquals(1, lijst.get(0).getAantalOverwinningen()),
            () -> assertEquals(1, lijst.get(1).getAantalOverwinningen()));
    }

    @Test
    void berekenLeaderboard_meerPuntenStaatEerst_sorteertAflopend() {
        Session sessie = sessieMet(new Player[] {emma, noah, lotte}, new int[] {5, 20, 12},
            new boolean[] {false, true, false});

        List<LeaderboardEntry> lijst = calculator.berekenLeaderboard(List.of(sessie));

        assertEquals(List.of(noah, lotte, emma),
            List.of(lijst.get(0).getSpeler(), lijst.get(1).getSpeler(), lijst.get(2).getSpeler()));
    }

    @Test
    void berekenLeaderboard_gelijkePuntenMeerOverwinningen_staatEerst() {
        Session s1 = sessieMet(new Player[] {emma, noah}, new int[] {10, 6}, new boolean[] {false, false});
        Session s2 = sessieMet(new Player[] {emma, noah}, new int[] {4, 8}, new boolean[] {false, true});
        // beide 14 punten, Noah heeft 1 overwinning

        List<LeaderboardEntry> lijst = calculator.berekenLeaderboard(List.of(s1, s2));

        assertEquals(noah, lijst.get(0).getSpeler());
    }

    @Test
    void berekenLeaderboard_gelijkePuntenEnOverwinningen_sorteertOpBijnaam() {
        Session sessie = sessieMet(new Player[] {noah, emma}, new int[] {10, 10}, new boolean[] {false, false});

        List<LeaderboardEntry> lijst = calculator.berekenLeaderboard(List.of(sessie));

        assertEquals(List.of(emma, noah), List.of(lijst.get(0).getSpeler(), lijst.get(1).getSpeler()));
    }

    // ---- hulpmethodes: zo blijft elke test kort en leesbaar

    private Player speler(String naam) {
        return new Player(naam, new User(naam, naam.toLowerCase() + "@example.com", "hash"));
    }

    private Session nieuweSessie() {
        return new Session(new Game("Catan", 2, 6, 90), LocalDate.of(2026, 11, 14), "Gent");
    }

    private Session sessieMet(Player[] spelers, int[] punten, boolean[] gewonnen) {
        Session sessie = nieuweSessie();
        for (Player s : spelers) {
            sessie.voegSpelerToe(s);
        }
        for (int i = 0; i < spelers.length; i++) {
            sessie.registreerUitslag(spelers[i], punten[i], gewonnen[i]);
        }
        return sessie;
    }
}
