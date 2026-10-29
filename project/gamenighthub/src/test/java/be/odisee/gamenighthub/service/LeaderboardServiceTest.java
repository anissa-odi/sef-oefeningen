package be.odisee.gamenighthub.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.odisee.gamenighthub.model.Game;
import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Session;
import be.odisee.gamenighthub.model.User;
import be.odisee.gamenighthub.repository.SessionRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaderboardServiceTest {
    private SessionRepository sessionRepository;
    private Session sessie;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        sessie = new Session(new Game("Catan", 2, 4, 90), LocalDate.of(2026, 11, 14), "Gent");
        Player emma = new Player("Emma", new User("Emma", "emma@example.com", "h"));
        Player noah = new Player("Noah", new User("Noah", "noah@example.com", "h"));
        sessie.voegSpelerToe(emma);
        sessie.voegSpelerToe(noah);
        sessie.registreerUitslag(emma, 5, false);
        sessie.registreerUitslag(noah, 9, true);
    }

    @Test
    void geefLeaderboard_nietsIngesteld_geeftLeegLeaderboard() {
        LeaderboardService service = new LeaderboardService(sessionRepository, new LeaderboardCalculator());

        // findAll() is niet ingesteld: een mock geeft een lege lijst terug
        assertTrue(service.geefLeaderboard().isEmpty());
    }

    @Test
    void geefLeaderboard_echteCalculator_geeftGesorteerdLeaderboard() {
        when(sessionRepository.findAll()).thenReturn(List.of(sessie));
        LeaderboardService service = new LeaderboardService(sessionRepository, new LeaderboardCalculator());

        List<LeaderboardEntry> lijst = service.geefLeaderboard();

        assertAll(
            () -> assertEquals(2, lijst.size()),
            () -> assertEquals("Noah", lijst.get(0).getSpeler().getBijnaam()),
            () -> assertEquals(9, lijst.get(0).getTotaalPunten()));
    }

    @Test
    void geefLeaderboard_spyCalculator_berekentOpAlleSessiesUitDeRepository() {
        List<Session> sessies = List.of(sessie);
        when(sessionRepository.findAll()).thenReturn(sessies);
        LeaderboardCalculator calculator = spy(new LeaderboardCalculator());
        LeaderboardService service = new LeaderboardService(sessionRepository, calculator);

        List<LeaderboardEntry> lijst = service.geefLeaderboard();

        verify(calculator).berekenLeaderboard(sessies);          // de service riep de calculator op
        assertEquals(2, lijst.size());                            // en de echte berekening draaide mee
    }

    @Test
    void geefLeaderboard_mockCalculator_geeftResultaatVanCalculatorDoor() {
        List<Session> sessies = List.of(sessie);
        List<LeaderboardEntry> verwacht = List.of();
        LeaderboardCalculator calculator = mock(LeaderboardCalculator.class);
        when(sessionRepository.findAll()).thenReturn(sessies);
        when(calculator.berekenLeaderboard(sessies)).thenReturn(verwacht);
        LeaderboardService service = new LeaderboardService(sessionRepository, calculator);

        assertSame(verwacht, service.geefLeaderboard());
    }
}
