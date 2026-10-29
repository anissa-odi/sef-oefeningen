package be.odisee.gamenighthub.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.odisee.gamenighthub.model.User;
import be.odisee.gamenighthub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

class AanmeldServiceTest {
    private UserRepository userRepository;
    private PasswordHasher passwordHasher;
    private AanmeldService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        service = new AanmeldService(userRepository, passwordHasher);
    }

    // ===== Deel 3, stap voor stap (samen) =====

    @Test
    void registreer_emailAlInGebruik_gooitExceptionEnBewaartNiets() {
        when(userRepository.bestaatEmail("emma@example.com")).thenReturn(true);

        assertThrows(IllegalStateException.class,
            () -> service.registreer("Emma", "emma@example.com", "geheim123"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registreer_nieuweGebruiker_bewaartGebruikerMetGehashtWachtwoord() {
        when(passwordHasher.hash("geheim123")).thenReturn("HASH-123");

        service.registreer("Emma", "emma@example.com", "geheim123");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User bewaard = captor.getValue();
        assertAll(
            () -> assertEquals("Emma", bewaard.getNaam()),
            () -> assertEquals("emma@example.com", bewaard.getEmail()),
            () -> assertEquals("HASH-123", bewaard.getWachtwoordHash()));
    }

    // ===== zelf =====

    @Test
    void registreer_nieuweGebruiker_bewaartNooitHetKlareWachtwoord() {
        when(passwordHasher.hash(anyString())).thenReturn("HASH");

        service.registreer("Emma", "emma@example.com", "geheim123");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertNotEquals("geheim123", captor.getValue().getWachtwoordHash());
    }

    @Test
    void registreer_nieuweGebruiker_geeftDeBewaardeGebruikerTerug() {
        when(passwordHasher.hash(anyString())).thenReturn("HASH");

        User resultaat = service.registreer("Emma", "emma@example.com", "geheim123");

        verify(userRepository).save(argThat(u -> u == resultaat));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abc", "1234567"})
    void registreer_teKortWachtwoord_gooitExceptionEnBewaartNiets(String wachtwoord) {
        assertThrows(IllegalArgumentException.class,
            () -> service.registreer("Emma", "emma@example.com", wachtwoord));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registreer_wachtwoordVanAchtTekens_isGeldig() {
        when(passwordHasher.hash("12345678")).thenReturn("HASH");

        User user = service.registreer("Emma", "emma@example.com", "12345678");

        assertEquals("HASH", user.getWachtwoordHash());
    }

    @Test
    void registreer_teKortWachtwoord_vraagGeenHashAan() {
        assertThrows(IllegalArgumentException.class,
            () -> service.registreer("Emma", "emma@example.com", "kort"));

        verify(passwordHasher, never()).hash(anyString());
    }

    @Test
    void meldAan_onbekendEmail_geeftNullEnControleertGeenWachtwoord() {
        // niets ingesteld: zoekOpEmail geeft null
        assertNull(service.meldAan("onbekend@example.com", "geheim123"));

        verify(passwordHasher, never()).matches(anyString(), anyString());
    }

    @Test
    void meldAan_juistWachtwoord_geeftGebruiker() {
        User emma = new User("Emma", "emma@example.com", "HASH-123");
        when(userRepository.zoekOpEmail("emma@example.com")).thenReturn(emma);
        when(passwordHasher.matches("geheim123", "HASH-123")).thenReturn(true);

        assertSame(emma, service.meldAan("emma@example.com", "geheim123"));
    }

    @Test
    void meldAan_foutWachtwoord_geeftNull() {
        User emma = new User("Emma", "emma@example.com", "HASH-123");
        when(userRepository.zoekOpEmail("emma@example.com")).thenReturn(emma);
        // matches niet ingesteld: geeft false

        assertNull(service.meldAan("emma@example.com", "fout"));
    }
}
