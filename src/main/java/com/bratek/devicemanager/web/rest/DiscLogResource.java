package com.bratek.devicemanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bratek.devicemanager.domain.DiscLog;

import com.bratek.devicemanager.repository.DiscLogRepository;
import com.bratek.devicemanager.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DiscLog.
 */
@RestController
@RequestMapping("/api")
public class DiscLogResource {

    private final Logger log = LoggerFactory.getLogger(DiscLogResource.class);

    private static final String ENTITY_NAME = "discLog";
        
    private final DiscLogRepository discLogRepository;

    public DiscLogResource(DiscLogRepository discLogRepository) {
        this.discLogRepository = discLogRepository;
    }

    /**
     * POST  /disc-logs : Create a new discLog.
     *
     * @param discLog the discLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discLog, or with status 400 (Bad Request) if the discLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/disc-logs")
    @Timed
    public ResponseEntity<DiscLog> createDiscLog(@Valid @RequestBody DiscLog discLog) throws URISyntaxException {
        log.debug("REST request to save DiscLog : {}", discLog);
        if (discLog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new discLog cannot already have an ID")).body(null);
        }
        DiscLog result = discLogRepository.save(discLog);
        return ResponseEntity.created(new URI("/api/disc-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /disc-logs : Updates an existing discLog.
     *
     * @param discLog the discLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discLog,
     * or with status 400 (Bad Request) if the discLog is not valid,
     * or with status 500 (Internal Server Error) if the discLog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/disc-logs")
    @Timed
    public ResponseEntity<DiscLog> updateDiscLog(@Valid @RequestBody DiscLog discLog) throws URISyntaxException {
        log.debug("REST request to update DiscLog : {}", discLog);
        if (discLog.getId() == null) {
            return createDiscLog(discLog);
        }
        DiscLog result = discLogRepository.save(discLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /disc-logs : get all the discLogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of discLogs in body
     */
    @GetMapping("/disc-logs")
    @Timed
    public List<DiscLog> getAllDiscLogs() {
        log.debug("REST request to get all DiscLogs");
        List<DiscLog> discLogs = discLogRepository.findAll();
        return discLogs;
    }

    /**
     * GET  /disc-logs/:id : get the "id" discLog.
     *
     * @param id the id of the discLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discLog, or with status 404 (Not Found)
     */
    @GetMapping("/disc-logs/{id}")
    @Timed
    public ResponseEntity<DiscLog> getDiscLog(@PathVariable Long id) {
        log.debug("REST request to get DiscLog : {}", id);
        DiscLog discLog = discLogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(discLog));
    }

    /**
     * DELETE  /disc-logs/:id : delete the "id" discLog.
     *
     * @param id the id of the discLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/disc-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDiscLog(@PathVariable Long id) {
        log.debug("REST request to delete DiscLog : {}", id);
        discLogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
