package be.odisee.gamenighthub.app;

import be.odisee.gamenighthub.model.Game;
import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Score;
import be.odisee.gamenighthub.model.Session;
import be.odisee.gamenighthub.model.User;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // gebruikers en spelers
        Player sara = new Player("Sara", new User("Sara Peeters", "sara@mail.be", "hash1"));
        Player tom = new Player("Tom", new User("Tom Janssens", "tom@mail.be", "hash2"));
        Player lien = new Player("Lien", new User("Lien Maes", "lien@mail.be", "hash3"));

        // spellen
        Game catan = new Game("Catan", 3, 4, 90);
        Game uno = new Game("Uno", 2, 10, 20);
        List<Game> collectie = List.of(catan, uno);

        // een game-avond
        Session avond = new Session(catan, LocalDate.of(2026, 10, 9), "Bij Sara thuis");
        avond.voegSpelerToe(sara);
        avond.voegSpelerToe(tom);
        avond.voegSpelerToe(lien);

        avond.registreerUitslag(sara, 10, true);
        avond.registreerUitslag(tom, 8, false);
        avond.registreerUitslag(lien, 6, false);

        // afdrukken
        System.out.println("Spellencollectie:");
        for (Game game : collectie) {
            System.out.println("- " + game);
        }

        System.out.printf("%nGame-avond op %s (%s) - %s%n",
                avond.getDatum(), avond.getLocatie(), avond.getGame().getTitel());
        System.out.println("Geschikt voor " + avond.getSpelers().size() + " spelers? "
                + catan.isGeschiktVoor(avond.getSpelers().size()));
        for (Score score : avond.getScores()) {
            System.out.println("- " + score);
        }
    }
}
