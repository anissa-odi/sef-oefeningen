package be.odisee.gamenighthub.model;

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
    public String toString() {
        return bijnaam;
    }
}
