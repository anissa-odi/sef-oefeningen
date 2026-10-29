package be.odisee.gamenighthub.repository;

import be.odisee.gamenighthub.model.Session;
import java.util.List;

public interface SessionRepository {
    void save(Session session);

    /** @return alle sessies, of een lege lijst */
    List<Session> findAll();
}
