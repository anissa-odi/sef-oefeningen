package be.odisee.gamenighthub.service;

public interface PasswordHasher {
    String hash(String wachtwoord);

    boolean matches(String wachtwoord, String hash);
}
