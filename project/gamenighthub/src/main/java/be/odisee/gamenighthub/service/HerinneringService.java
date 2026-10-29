package be.odisee.gamenighthub.service;

import be.odisee.gamenighthub.model.Player;
import be.odisee.gamenighthub.model.Session;
import be.odisee.gamenighthub.repository.SessionRepository;
import java.time.Clock;
import java.time.LocalDate;

public class HerinneringService {
    private final Mailer mailer;
    private final SessionRepository sessionRepository;
    private final Clock clock;

    public HerinneringService(Mailer mailer, SessionRepository sessionRepository, Clock clock) {
        this.mailer = mailer;
        this.sessionRepository = sessionRepository;
        this.clock = clock;
    }

    /** Mailt alle deelnemers van sessies die morgen plaatsvinden. @return het aantal verstuurde mails */
    public int stuurHerinneringen() {
        LocalDate morgen = LocalDate.now(clock).plusDays(1);
        int verstuurd = 0;
        for (Session sessie : sessionRepository.findAll()) {
            if (!sessie.getDatum().equals(morgen)) {
                continue;
            }
            for (Player speler : sessie.getSpelers()) {
                mailer.verstuur(speler.getUser().getEmail(),
                    "Herinnering: " + sessie.getGame().getTitel() + " morgen",
                    "Vergeet niet: morgen speel je in " + sessie.getLocatie() + ".");
                verstuurd++;
            }
        }
        return verstuurd;
    }
}
