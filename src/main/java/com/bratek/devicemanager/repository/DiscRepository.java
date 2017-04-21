package com.bratek.devicemanager.repository;

import com.bratek.devicemanager.domain.Disc;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Disc entity.
 */
@SuppressWarnings("unused")
public interface DiscRepository extends JpaRepository<Disc,Long> {

}
