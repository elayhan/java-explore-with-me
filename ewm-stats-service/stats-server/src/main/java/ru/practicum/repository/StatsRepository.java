package ru.practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.model.ViewStats(eph.app, eph.uri, count(eph.ip)) " +
            "from EndpointHit eph " +
            "where eph.timestamp between :start and :end " +
            "and eph.uri in :uris or :uris is null " +
            "group by eph.app, eph.uri " +
            "order by 3 desc")
    List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.model.ViewStats(eph.app, eph.uri, count(distinct eph.ip)) " +
            "from EndpointHit eph " +
            "where eph.timestamp between :start and :end " +
            "and eph.uri in :uris or :uris is null " +
            "group by eph.app, eph.uri " +
            "order by 3 desc")
    List<ViewStats> findUniqueViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
