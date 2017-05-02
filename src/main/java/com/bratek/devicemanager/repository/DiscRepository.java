package com.bratek.devicemanager.repository;

import com.bratek.devicemanager.domain.Disc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Disc entity.
 */

public interface DiscRepository extends JpaRepository<Disc,Long> {

    @Query("select disc from Disc disc where disc.connection.user.login = ?#{principal.username}")
    Page<Disc> findByUserIsCurrentUser(Pageable pageable);}
