package com.bratek.devicemanager.repository;

import com.bratek.devicemanager.domain.Connection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Connection entity.
 */
@SuppressWarnings("unused")
public interface ConnectionRepository extends JpaRepository<Connection,Long> {

    @Query("select connection from Connection connection where connection.user.login = ?#{principal.username}")
    List<Connection> findByUserIsCurrentUser();

    @Query("select connection from Connection connection where connection.user.login = ?#{principal.username}")
    Page<Connection> findByUserIsCurrentUser(Pageable pageable);

    @Query("select connection from Connection connection where connection.user.login = ?#{principal.username}")
    List<Connection> findByUserIsCurrentUser(String login);

}
