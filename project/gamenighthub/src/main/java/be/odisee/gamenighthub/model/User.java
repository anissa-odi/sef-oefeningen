package be.odisee.gamenighthub.model;

import java.util.UUID;

public class User {
    private UUID id;
    private String naam;
    private String email;
    private String wachtwoordHash;

    public User(String naam, String email, String wachtwoordHash) {
        this.id = UUID.randomUUID();
        this.naam = naam;
        this.email = email;
        this.wachtwoordHash = wachtwoordHash;
    }

    public UUID getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWachtwoordHash() {
        return wachtwoordHash;
    }

    @Override
    public String toString() {
        return naam + " (" + email + ")";
    }
}
