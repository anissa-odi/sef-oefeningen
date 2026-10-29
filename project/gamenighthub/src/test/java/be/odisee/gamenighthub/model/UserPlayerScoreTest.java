package be.odisee.gamenighthub.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UserPlayerScoreTest {

    // ===== Deel 1: User =====

    @Test
    void constructor_geldigeGegevens_bewaartGegevens() {
        User user = new User("Emma", "emma@example.com", "hash");

        assertAll(
            () -> assertEquals("Emma", user.getNaam()),
            () -> assertEquals("emma@example.com", user.getEmail()),
            () -> assertEquals("hash", user.getWachtwoordHash()));
    }

    @Test
    void equals_zelfdeEmailAndereNaam_geeftTrue() {
        User a = new User("Emma", "emma@example.com", "hash1");
        User b = new User("E. Peeters", "emma@example.com", "hash2");

        assertEquals(a, b);
    }

    @Test
    void equals_andereEmail_geeftFalse() {
        assertNotEquals(new User("Emma", "emma@example.com", "h"), new User("Emma", "emma2@example.com", "h"));
    }

    // ===== Deel 1: Player =====

    @Test
    void playerEquals_zelfdeUserAndereBijnaam_geeftTrue() {
        User user = new User("Emma", "emma@example.com", "h");

        assertEquals(new Player("Em", user), new Player("Emmie", user));
    }

    @Test
    void playerEquals_andereUser_geeftFalse() {
        Player a = new Player("Emma", new User("Emma", "emma@example.com", "h"));
        Player b = new Player("Emma", new User("Noah", "noah@example.com", "h"));

        assertNotEquals(a, b);
    }

    @Test
    void playerHashCode_gelijkeSpelers_geeftZelfdeHashCode() {
        User user = new User("Emma", "emma@example.com", "h");

        assertEquals(new Player("Em", user).hashCode(), new Player("Emmie", user).hashCode());
    }

    // ===== Deel 2: Score =====

    @Test
    void scoreConstructor_negatievePunten_gooitException() {
        Player speler = new Player("Emma", new User("Emma", "emma@example.com", "h"));

        assertThrows(IllegalArgumentException.class, () -> new Score(speler, -1, false));
    }

    @Test
    void scoreConstructor_playerNull_gooitException() {
        assertThrows(IllegalArgumentException.class, () -> new Score(null, 5, false));
    }
}
