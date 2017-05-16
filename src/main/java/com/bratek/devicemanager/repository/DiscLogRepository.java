package com.bratek.devicemanager.repository;

import com.bratek.devicemanager.domain.DiscLog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DiscLog entity.
 */
@SuppressWarnings("unused")
public interface DiscLogRepository extends JpaRepository<DiscLog,Long> {

    List<DiscLog> findByDiscIdOrderByDateDesc(Long discId);
}
