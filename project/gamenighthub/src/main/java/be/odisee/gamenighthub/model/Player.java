package be.odisee.gamenighthub.model;

import java.util.Objects;

public class Player {
    private String bijnaam;
    private User user;

    public Player(String bijnaam, User user) {
        this.bijnaam = bijnaam;
        this.user = user;
    }

    public String getBijnaam() {
        return bijnaam;
    }

    public void setBijnaam(String bijnaam) {
        this.bijnaam = bijnaam;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;
        return Objects.equals(user, player.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }
}
