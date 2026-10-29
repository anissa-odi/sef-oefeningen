package be.odisee.gamenighthub.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.odisee.gamenighthub.model.Game;
import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Session;
import be.odisee.gamenighthub.model.User;
import be.odisee.gamenighthub.repository.GameRepository;
import be.odisee.gamenighthub.repository.SessionRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SessionServiceTest {
    private static final LocalDate DATUM = LocalDate.of(2026, 11, 14);

    private SessionRepository sessionRepository;
    private GameRepository gameRepository;
    private SessionService service;

    private Game catan;
    private Player emma;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        gameRepository = mock(GameRepository.class);
        service = new SessionService(sessionRepository, gameRepository);
        catan = new Game("Catan", 2, 3, 90);
        emma = new Player("Emma", new User("Emma", "emma@example.com", "h"));
    }

    // ----- plan

    @Test
    void plan_onbekendSpel_gooitExceptionEnBewaartNiets() {
        assertThrows(IllegalArgumentException.class, () -> service.plan("Onbekend", DATUM, "Gent"));

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void plan_bekendSpel_bewaartSessieMetSpelDatumEnLocatie() {
        when(gameRepository.zoekOpTitel("Catan")).thenReturn(catan);

        service.plan("Catan", DATUM, "Gent");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());
        Session bewaard = captor.getValue();
        assertAll(
            () -> assertSame(catan, bewaard.getGame()),
            () -> assertEquals(DATUM, bewaard.getDatum()),
            () -> assertEquals("Gent", bewaard.getLocatie()),
            () -> assertTrue(bewaard.getSpelers().isEmpty()));
    }

    @Test
    void plan_bekendSpel_geeftDeBewaardeSessieTerug() {
        when(gameRepository.zoekOpTitel(anyString())).thenReturn(catan);

        Session resultaat = service.plan("Catan", DATUM, "Gent");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());
        assertSame(captor.getValue(), resultaat);
    }

    // ----- voegSpelerToe
}
