package be.odisee.gamenighthub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.odisee.gamenighthub.model.Game;
import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Session;
import be.odisee.gamenighthub.model.User;
import be.odisee.gamenighthub.repository.SessionRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class HerinneringServiceTest {
    private static final ZoneId ZONE = ZoneId.of("Europe/Brussels");
    private static final LocalDate VANDAAG = LocalDate.of(2026, 11, 13);

    private Mailer mailer;
    private SessionRepository sessionRepository;
    private HerinneringService service;
    private Game catan;

    @BeforeEach
    void setUp() {
        mailer = mock(Mailer.class);
        sessionRepository = mock(SessionRepository.class);
        // een vaste klok: een echt object, geen mock. Zo is "vandaag" in de test altijd 13 november 2026.
        Clock clock = Clock.fixed(VANDAAG.atStartOfDay(ZONE).toInstant(), ZONE);
        service = new HerinneringService(mailer, sessionRepository, clock);
        catan = new Game("Catan", 2, 4, 90);
    }

    @Test
    void stuurHerinneringen_geenSessies_verstuurtNietsEnGeeftNul() {
        // findAll() niet ingesteld: lege lijst
        assertEquals(0, service.stuurHerinneringen());

        verify(mailer, never()).verstuur(anyString(), anyString(), anyString());
    }

    @Test
    void stuurHerinneringen_sessieMorgen_mailtDeelnemers() {
        Session morgen = sessieOp(VANDAAG.plusDays(1), "emma@example.com", "noah@example.com");
        when(sessionRepository.findAll()).thenReturn(List.of(morgen));

        int aantal = service.stuurHerinneringen();

        ArgumentCaptor<String> naar = ArgumentCaptor.forClass(String.class);
        verify(mailer, times(2)).verstuur(naar.capture(), anyString(), anyString());
        assertEquals(2, aantal);
        assertEquals(List.of("emma@example.com", "noah@example.com"), naar.getAllValues());
    }

    @Test
    void stuurHerinneringen_sessieVandaagOfOverTweeDagen_verstuurtNiets() {
        Session vandaag = sessieOp(VANDAAG, "emma@example.com");
        Session overTweeDagen = sessieOp(VANDAAG.plusDays(2), "noah@example.com");
        when(sessionRepository.findAll()).thenReturn(List.of(vandaag, overTweeDagen));

        assertEquals(0, service.stuurHerinneringen());

        verify(mailer, never()).verstuur(anyString(), anyString(), anyString());
    }

    @Test
    void stuurHerinneringen_gemengdeSessies_mailtEnkelDeelnemersVanMorgen() {
        Session vandaag = sessieOp(VANDAAG, "emma@example.com");
        Session morgen = sessieOp(VANDAAG.plusDays(1), "noah@example.com");
        when(sessionRepository.findAll()).thenReturn(List.of(vandaag, morgen));

        service.stuurHerinneringen();

        verify(mailer).verstuur(eq("noah@example.com"), anyString(), anyString());
        verify(mailer, never()).verstuur(eq("emma@example.com"), anyString(), anyString());
    }

    private Session sessieOp(LocalDate datum, String... emails) {
        Session sessie = new Session(catan, datum, "Gent");
        for (String email : emails) {
            String naam = email.substring(0, email.indexOf('@'));
            sessie.voegSpelerToe(new Player(naam, new User(naam, email, "h")));
        }
        return sessie;
    }
}
