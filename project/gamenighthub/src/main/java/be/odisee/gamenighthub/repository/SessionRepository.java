package be.odisee.gamenighthub.repository;

import be.odisee.gamenighthub.model.Session;

import java.time.LocalDate;
import java.util.List;

public interface SessionRepository {
    void save(Session session);
    List<Session> findAll();
    List<Session> findByDatum(LocalDate datum);
    void delete(Session session);
}
