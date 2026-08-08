package com.breach.engine.repository;

import com.breach.engine.model.PublicBreachRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicBreachRepository extends JpaRepository<PublicBreachRecord, Long> {

    Optional<PublicBreachRecord> findByTitleIgnoreCase(String title);

    List<PublicBreachRecord> findByDomainContainingIgnoreCase(String domain);

    List<PublicBreachRecord> findByDataClassesContainingIgnoreCase(String dataClass);

    List<PublicBreachRecord> findBySeverityIgnoreCase(String severity);
}
