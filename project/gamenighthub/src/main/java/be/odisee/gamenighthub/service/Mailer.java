package be.odisee.gamenighthub.service;

public interface Mailer {
    void verstuur(String naar, String onderwerp, String tekst);
}
