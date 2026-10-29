package be.odisee.gamenighthub.service;

import be.odisee.gamenighthub.model.User;
import be.odisee.gamenighthub.repository.UserRepository;

public class AanmeldService {
    static final int MIN_WACHTWOORD_LENGTE = 8;

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public AanmeldService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User registreer(String naam, String email, String wachtwoord) {
        if (wachtwoord == null || wachtwoord.length() < MIN_WACHTWOORD_LENGTE) {
            throw new IllegalArgumentException("wachtwoord moet minstens 8 tekens bevatten");
        }
        if (userRepository.bestaatEmail(email)) {
            throw new IllegalStateException("e-mailadres is al in gebruik");
        }
        User user = new User(naam, email, passwordHasher.hash(wachtwoord));
        userRepository.save(user);
        return user;
    }

    /** @return de gebruiker, of null bij een onbekend e-mailadres of een fout wachtwoord */
    public User meldAan(String email, String wachtwoord) {
        User user = userRepository.zoekOpEmail(email);
        if (user == null) {
            return null;
        }
        return passwordHasher.matches(wachtwoord, user.getWachtwoordHash()) ? user : null;
    }
}
