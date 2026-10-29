package be.odisee.gamenighthub.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import be.odisee.gamenighthub.model.Game;
import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Session;
import be.odisee.gamenighthub.model.User;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UitnodigingServiceTest {
    private Mailer mailer;
    private UitnodigingService service;
    private Session sessie;

    @BeforeEach
    void setUp() {
        mailer = mock(Mailer.class);
        service = new UitnodigingService(mailer);
        sessie = new Session(new Game("Catan", 2, 4, 90), LocalDate.of(2026, 11, 14), "Gent");
    }

    @Test
    void stuurUitnodigingen_sessieZonderDeelnemers_verstuurtGeenMails() {
        service.stuurUitnodigingen(sessie);

        verify(mailer, never()).verstuur(anyString(), anyString(), anyString());
    }

    @Test
    void stuurUitnodigingen_eenDeelnemer_mailtDieDeelnemer() {
        sessie.voegSpelerToe(speler("Emma"));

        service.stuurUitnodigingen(sessie);

        verify(mailer).verstuur(eq("emma@example.com"), contains("Catan"), anyString());
    }

    @Test
    void stuurUitnodigingen_meerdereDeelnemers_mailtIedereenEenKeer() {
        sessie.voegSpelerToe(speler("Emma"));
        sessie.voegSpelerToe(speler("Noah"));
        sessie.voegSpelerToe(speler("Lotte"));

        service.stuurUitnodigingen(sessie);

        ArgumentCaptor<String> naar = ArgumentCaptor.forClass(String.class);
        verify(mailer, times(3)).verstuur(naar.capture(), anyString(), anyString());
        assertEquals(List.of("emma@example.com", "noah@example.com", "lotte@example.com"), naar.getAllValues());
    }

    @Test
    void stuurUitnodigingen_deelnemer_vermeldtDatumEnLocatieInDeTekst() {
        sessie.voegSpelerToe(speler("Emma"));

        service.stuurUitnodigingen(sessie);

        ArgumentCaptor<String> tekst = ArgumentCaptor.forClass(String.class);
        verify(mailer).verstuur(anyString(), anyString(), tekst.capture());
        assertAll(
            () -> assertTrue(tekst.getValue().contains("2026-11-14")),
            () -> assertTrue(tekst.getValue().contains("Gent")));
    }

    @Test
    void stuurUitnodigingen_mailFaaltVoorEenDeelnemer_mailtDeAndereDeelnemersNogSteeds() {
        sessie.voegSpelerToe(speler("Emma"));
        sessie.voegSpelerToe(speler("Noah"));
        doThrow(new IllegalStateException("smtp down"))
            .when(mailer).verstuur(eq("emma@example.com"), anyString(), anyString());

        service.stuurUitnodigingen(sessie);

        verify(mailer).verstuur(eq("noah@example.com"), anyString(), anyString());
    }

    private Player speler(String naam) {
        return new Player(naam, new User(naam, naam.toLowerCase() + "@example.com", "h"));
    }
}
