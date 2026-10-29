package be.odisee.gamenighthub.service;

import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Session;

public class UitnodigingService {
    private final Mailer mailer;

    public UitnodigingService(Mailer mailer) {
        this.mailer = mailer;
    }

    /** Stuurt een mail naar elke deelnemer van de sessie. */
    public void stuurUitnodigingen(Session sessie) {
        String onderwerp = "Game-avond: " + sessie.getGame().getTitel();
        String tekst = "Je bent uitgenodigd op " + sessie.getDatum() + " in " + sessie.getLocatie() + ".";
        for (Player speler : sessie.getSpelers()) {
            try {
                mailer.verstuur(speler.getUser().getEmail(), onderwerp, tekst);
            } catch (RuntimeException e) {
                // één mislukte mail mag de andere deelnemers niet blokkeren
            }
        }
    }
}
