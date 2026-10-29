package be.odisee.gamenighthub.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class GameTest {

    // ===== Deel 1: bestaand gedrag =====

    @Test
    void constructor_geldigeGegevens_bewaartGegevens() {
        Game game = new Game("Catan", 3, 4, 90);

        assertAll(
            () -> assertEquals("Catan", game.getTitel()),
            () -> assertEquals(3, game.getMinSpelers()),
            () -> assertEquals(4, game.getMaxSpelers()),
            () -> assertEquals(90, game.getGemiddeldeSpeelduur()));
    }

    @Test
    void equals_zelfdeTitelAndereDetails_geeftTrue() {
        Game a = new Game("Catan", 3, 4, 90);
        Game b = new Game("Catan", 2, 6, 30);

        assertEquals(a, b);
    }

    @Test
    void equals_andereTitel_geeftFalse() {
        assertNotEquals(new Game("Catan", 3, 4, 90), new Game("Risk", 3, 4, 90));
    }

    @Test
    void equals_null_geeftFalse() {
        assertFalse(new Game("Catan", 3, 4, 90).equals(null));
    }

    @Test
    void equals_andereKlasse_geeftFalse() {
        assertFalse(new Game("Catan", 3, 4, 90).equals("Catan"));
    }

    @Test
    void hashCode_gelijkeSpellen_geeftZelfdeHashCode() {
        assertEquals(new Game("Catan", 3, 4, 90).hashCode(), new Game("Catan", 2, 6, 30).hashCode());
    }

    // ===== Deel 2: TDD =====

    @ParameterizedTest
    @CsvSource({
        "2, false",   // net onder het minimum
        "3, true",    // minimum
        "4, true",    // midden
        "5, true",    // maximum
        "6, false"    // net boven het maximum
    })
    void isGeschiktVoor_aantalSpelers_geeftVerwachtResultaat(int aantal, boolean verwacht) {
        Game game = new Game("Catan", 3, 5, 90);

        assertEquals(verwacht, game.isGeschiktVoor(aantal));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void constructor_legeOfBlancoTitel_gooitException(String titel) {
        assertThrows(IllegalArgumentException.class, () -> new Game(titel, 2, 4, 60));
    }

    @Test
    void constructor_titelNull_gooitException() {
        assertThrows(IllegalArgumentException.class, () -> new Game(null, 2, 4, 60));
    }

    @Test
    void constructor_minSpelersNul_gooitException() {
        assertThrows(IllegalArgumentException.class, () -> new Game("Catan", 0, 4, 60));
    }

    @Test
    void constructor_minSpelersEen_isGeldig() {
        assertEquals(1, new Game("Patience", 1, 1, 15).getMinSpelers());
    }

    @Test
    void constructor_maxKleinerDanMin_gooitException() {
        assertThrows(IllegalArgumentException.class, () -> new Game("Catan", 4, 3, 60));
    }

    @Test
    void constructor_maxGelijkAanMin_isGeldig() {
        assertTrue(new Game("Duel", 2, 2, 30).isGeschiktVoor(2));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -30})
    void constructor_speelduurNietPositief_gooitException(int speelduur) {
        assertThrows(IllegalArgumentException.class, () -> new Game("Catan", 3, 4, speelduur));
    }

    // ===== Deel 2, uitdaging: equals ongevoelig voor hoofdletters =====

    @Test
    void equals_zelfdeTitelAndereHoofdletters_geeftTrue() {
        assertEquals(new Game("Catan", 3, 4, 90), new Game("CATAN", 3, 4, 90));
    }

    @Test
    void hashCode_zelfdeTitelAndereHoofdletters_geeftZelfdeHashCode() {
        assertEquals(new Game("Catan", 3, 4, 90).hashCode(), new Game("CATAN", 3, 4, 90).hashCode());
    }
}
