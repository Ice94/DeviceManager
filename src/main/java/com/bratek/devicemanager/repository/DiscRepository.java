package com.bratek.devicemanager.repository;

import com.bratek.devicemanager.domain.Connection;
import com.bratek.devicemanager.domain.Disc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Disc entity.
 */

public interface DiscRepository extends JpaRepository<Disc,Long> {

    @Query("select disc from Disc disc where disc.connection.user.login = ?#{principal.username}")
    Page<Disc> findByUserIsCurrentUser(Pageable pageable);

    List<Disc> findAllByConnection(Connection connection);
}
