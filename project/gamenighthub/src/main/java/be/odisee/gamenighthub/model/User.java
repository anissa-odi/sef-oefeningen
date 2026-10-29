package be.odisee.gamenighthub.model;

import java.util.Objects;
import java.util.UUID;

public class User {
    private int id;
    private String naam;
    private String email;
    private String wachtwoordHash;

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
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

    public void setWachtwoordHash(String wachtwoordHash) {
        this.wachtwoordHash = wachtwoordHash;
    }

    public User(String naam, String email, String wachtwoordHash){
        this.naam = naam;
        this.email = email;
        this.wachtwoordHash = wachtwoordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
