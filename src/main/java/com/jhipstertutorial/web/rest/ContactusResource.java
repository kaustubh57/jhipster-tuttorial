package com.jhipstertutorial.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipstertutorial.domain.Contactus;

import com.jhipstertutorial.repository.ContactusRepository;
import com.jhipstertutorial.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contactus.
 */
@RestController
@RequestMapping("/api")
public class ContactusResource {

    private final Logger log = LoggerFactory.getLogger(ContactusResource.class);

    @Inject
    private ContactusRepository contactusRepository;

    /**
     * POST  /contactus : Create a new contactus.
     *
     * @param contactus the contactus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactus, or with status 400 (Bad Request) if the contactus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contactus")
    @Timed
    public ResponseEntity<Contactus> createContactus(@Valid @RequestBody Contactus contactus) throws URISyntaxException {
        log.debug("REST request to save Contactus : {}", contactus);
        if (contactus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contactus", "idexists", "A new contactus cannot already have an ID")).body(null);
        }
        Contactus result = contactusRepository.save(contactus);
        return ResponseEntity.created(new URI("/api/contactus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contactus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contactus : Updates an existing contactus.
     *
     * @param contactus the contactus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactus,
     * or with status 400 (Bad Request) if the contactus is not valid,
     * or with status 500 (Internal Server Error) if the contactus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contactus")
    @Timed
    public ResponseEntity<Contactus> updateContactus(@Valid @RequestBody Contactus contactus) throws URISyntaxException {
        log.debug("REST request to update Contactus : {}", contactus);
        if (contactus.getId() == null) {
            return createContactus(contactus);
        }
        Contactus result = contactusRepository.save(contactus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contactus", contactus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contactus : get all the contactus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contactus in body
     */
    @GetMapping("/contactus")
    @Timed
    public List<Contactus> getAllcontactus() {
        log.debug("REST request to get all contactus");
        List<Contactus> contactus = contactusRepository.findAll();
        return contactus;
    }

    /**
     * GET  /contactus/:id : get the "id" contactus.
     *
     * @param id the id of the contactus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactus, or with status 404 (Not Found)
     */
    @GetMapping("/contactus/{id}")
    @Timed
    public ResponseEntity<Contactus> getContactus(@PathVariable Long id) {
        log.debug("REST request to get Contactus : {}", id);
        Contactus contactus = contactusRepository.findOne(id);
        return Optional.ofNullable(contactus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contactus/:id : delete the "id" contactus.
     *
     * @param id the id of the contactus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contactus/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactus(@PathVariable Long id) {
        log.debug("REST request to delete Contactus : {}", id);
        contactusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contactus", id.toString())).build();
    }

}
