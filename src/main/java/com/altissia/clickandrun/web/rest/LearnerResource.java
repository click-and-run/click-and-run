package com.altissia.clickandrun.web.rest;

import com.altissia.clickandrun.domain.Learner;
import com.altissia.clickandrun.repository.LearnerRepository;
import com.altissia.clickandrun.web.rest.util.HeaderUtil;
import com.altissia.clickandrun.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Learner.
 */
@RestController
@RequestMapping("/api")
public class LearnerResource {

    private final Logger log = LoggerFactory.getLogger(LearnerResource.class);

    private static final String ENTITY_NAME = "learner";

    private final LearnerRepository learnerRepository;

    public LearnerResource(LearnerRepository learnerRepository) {
        this.learnerRepository = learnerRepository;
    }

    /**
     * POST  /learners : Create a new learner.
     *
     * @param learner the learner to create
     * @return the ResponseEntity with status 201 (Created) and with body the new learner, or with status 400 (Bad Request) if the learner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/learners")
    @Timed
    public ResponseEntity<Learner> createLearner(@Valid @RequestBody Learner learner) throws URISyntaxException {
        log.debug("REST request to save Learner : {}", learner);
        if (learner.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new learner cannot already have an ID")).body(null);
        }
        Learner result = learnerRepository.save(learner);
        return ResponseEntity.created(new URI("/api/learners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /learners : Updates an existing learner.
     *
     * @param learner the learner to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated learner,
     * or with status 400 (Bad Request) if the learner is not valid,
     * or with status 500 (Internal Server Error) if the learner couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/learners")
    @Timed
    public ResponseEntity<Learner> updateLearner(@Valid @RequestBody Learner learner) throws URISyntaxException {
        log.debug("REST request to update Learner : {}", learner);
        if (learner.getId() == null) {
            return createLearner(learner);
        }
        Learner result = learnerRepository.save(learner);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, learner.getId().toString()))
            .body(result);
    }

    /**
     * GET  /learners : get all the learners.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of learners in body
     */
    @GetMapping("/learners")
    @Timed
    public ResponseEntity<List<Learner>> getAllLearners(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Learners");
        Page<Learner> page = learnerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/learners");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /learners/:id : get the "id" learner.
     *
     * @param id the id of the learner to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the learner, or with status 404 (Not Found)
     */
    @GetMapping("/learners/{id}")
    @Timed
    public ResponseEntity<Learner> getLearner(@PathVariable Long id) {
        log.debug("REST request to get Learner : {}", id);
        Learner learner = learnerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(learner));
    }

    /**
     * DELETE  /learners/:id : delete the "id" learner.
     *
     * @param id the id of the learner to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/learners/{id}")
    @Timed
    public ResponseEntity<Void> deleteLearner(@PathVariable Long id) {
        log.debug("REST request to delete Learner : {}", id);
        learnerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
