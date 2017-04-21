package com.bratek.devicemanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bratek.devicemanager.domain.Disc;

import com.bratek.devicemanager.repository.DiscRepository;
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
 * REST controller for managing Disc.
 */
@RestController
@RequestMapping("/api")
public class DiscResource {

    private final Logger log = LoggerFactory.getLogger(DiscResource.class);

    private static final String ENTITY_NAME = "disc";
        
    private final DiscRepository discRepository;

    public DiscResource(DiscRepository discRepository) {
        this.discRepository = discRepository;
    }

    /**
     * POST  /discs : Create a new disc.
     *
     * @param disc the disc to create
     * @return the ResponseEntity with status 201 (Created) and with body the new disc, or with status 400 (Bad Request) if the disc has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/discs")
    @Timed
    public ResponseEntity<Disc> createDisc(@Valid @RequestBody Disc disc) throws URISyntaxException {
        log.debug("REST request to save Disc : {}", disc);
        if (disc.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new disc cannot already have an ID")).body(null);
        }
        Disc result = discRepository.save(disc);
        return ResponseEntity.created(new URI("/api/discs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discs : Updates an existing disc.
     *
     * @param disc the disc to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated disc,
     * or with status 400 (Bad Request) if the disc is not valid,
     * or with status 500 (Internal Server Error) if the disc couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/discs")
    @Timed
    public ResponseEntity<Disc> updateDisc(@Valid @RequestBody Disc disc) throws URISyntaxException {
        log.debug("REST request to update Disc : {}", disc);
        if (disc.getId() == null) {
            return createDisc(disc);
        }
        Disc result = discRepository.save(disc);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, disc.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discs : get all the discs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of discs in body
     */
    @GetMapping("/discs")
    @Timed
    public List<Disc> getAllDiscs() {
        log.debug("REST request to get all Discs");
        List<Disc> discs = discRepository.findAll();
        return discs;
    }

    /**
     * GET  /discs/:id : get the "id" disc.
     *
     * @param id the id of the disc to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the disc, or with status 404 (Not Found)
     */
    @GetMapping("/discs/{id}")
    @Timed
    public ResponseEntity<Disc> getDisc(@PathVariable Long id) {
        log.debug("REST request to get Disc : {}", id);
        Disc disc = discRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(disc));
    }

    /**
     * DELETE  /discs/:id : delete the "id" disc.
     *
     * @param id the id of the disc to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/discs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDisc(@PathVariable Long id) {
        log.debug("REST request to delete Disc : {}", id);
        discRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
