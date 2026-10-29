package be.odisee.gamenighthub.repository;

import be.odisee.gamenighthub.model.User;

public interface UserRepository {
    boolean bestaatEmail(String email);

    /** @return de gebruiker, of null als het e-mailadres onbekend is */
    User zoekOpEmail(String email);

    void save(User user);
}
