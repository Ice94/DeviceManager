package com.bratek.devicemanager.web.rest;

import com.bratek.devicemanager.SSHConnector.SSHConnector;
import com.bratek.devicemanager.domain.Connection;
import com.bratek.devicemanager.domain.Disc;
import com.bratek.devicemanager.repository.ConnectionRepository;
import com.bratek.devicemanager.repository.DiscLogRepository;
import com.bratek.devicemanager.repository.DiscRepository;
import com.bratek.devicemanager.repository.UserRepository;
import com.bratek.devicemanager.security.AuthoritiesConstants;
import com.bratek.devicemanager.security.SecurityUtils;
import com.bratek.devicemanager.web.rest.util.HeaderUtil;
import com.bratek.devicemanager.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import com.jcraft.jsch.JSchException;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Connection.
 */
@RestController
@RequestMapping("/api")
public class ConnectionResource {

    private final Logger log = LoggerFactory.getLogger(ConnectionResource.class);

    private static final String ENTITY_NAME = "connection";

    private final ConnectionRepository connectionRepository;

    private  DiscResource discResource;

    private SSHConnector sshConnector;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscRepository discRepository;

    @Autowired
    private DiscLogRepository discLogRepository;

    private static boolean alreadyExecuted = false;

    public ConnectionResource(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    /**
     * POST  /connections : Create a new connection.
     *
     * @param connection the connection to create
     * @return the ResponseEntity with status 201 (Created) and with body the new connection, or with status 400 (Bad Request) if the connection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/connections")
    @Timed
    public ResponseEntity<Connection> createConnection(@Valid @RequestBody Connection connection) throws URISyntaxException, IOException, JSchException {
        discResource = new DiscResource(discRepository);

        log.debug("REST request to save Connection : {}", connection);
        if (connection.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new connection cannot already have an ID")).body(null);
        }
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            connection.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        }
        Connection result = connectionRepository.save(connection);
        sshConnector = new SSHConnector(result.getUserHost(), result.getPassword());
        List<String> names = sshConnector.getDiscNames();
        List<Disc> discs = new ArrayList<>();

        for (String tmp: names) {
            Disc disc = new Disc();
            disc.setConnection(result);
            disc.setName(tmp);
            discs.add(disc);
            discRepository.save(disc);
        }

        return ResponseEntity.created(new URI("/api/connections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /connections : Updates an existing connection.
     *
     * @param connection the connection to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated connection,
     * or with status 400 (Bad Request) if the connection is not valid,
     * or with status 500 (Internal Server Error) if the connection couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/connections")
    @Timed
    public ResponseEntity<Connection> updateConnection(@Valid @RequestBody Connection connection) throws URISyntaxException, IOException, JSchException {
        log.debug("REST request to update Connection : {}", connection);
        if (connection.getId() == null) {
            return createConnection(connection);
        }
        Connection result = connectionRepository.save(connection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, connection.getId().toString()))
            .body(result);
    }

    /**
     * GET  /connections : get all the connections.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of connections in body
     */



    /**
     * GET  /connections/:id : get the "id" connection.
     *
     * @param id the id of the connection to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the connection, or with status 404 (Not Found)
     */
    @GetMapping("/connections/{id}")
    @Timed
    public ResponseEntity<Connection> getConnection(@PathVariable Long id) {
        log.debug("REST request to get Connection : {}", id);
        Connection connection = connectionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(connection));
    }

    /**
     * DELETE  /connections/:id : delete the "id" connection.
     *
     * @param id the id of the connection to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/connections/{id}")
    @Timed
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        log.debug("REST request to delete Connection : {}", id);
        connectionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/connections")
    @Timed
    public ResponseEntity<List<Connection>> getAllConnections(Pageable pageable) throws URISyntaxException{
        log.debug("REST request to get a page of Points");
        Page<Connection> page;
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            page = connectionRepository.findByUserIsCurrentUser(pageable);
        }
        else{
            page = connectionRepository.findByUserIsCurrentUser(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/connections");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
