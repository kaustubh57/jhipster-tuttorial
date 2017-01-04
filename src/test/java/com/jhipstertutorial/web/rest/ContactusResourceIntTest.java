package com.jhipstertutorial.web.rest;

import com.jhipstertutorial.JhipstertutorialApp;

import com.jhipstertutorial.domain.Contactus;
import com.jhipstertutorial.repository.ContactusRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContactusResource REST controller.
 *
 * @see ContactusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipstertutorialApp.class)
public class ContactusResourceIntTest {

    private static final String DEFAULT_CONTACTNAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Inject
    private ContactusRepository contactusRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContactusMockMvc;

    private Contactus contactus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactusResource contactusResource = new ContactusResource();
        ReflectionTestUtils.setField(contactusResource, "contactusRepository", contactusRepository);
        this.restContactusMockMvc = MockMvcBuilders.standaloneSetup(contactusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contactus createEntity(EntityManager em) {
        Contactus contactus = new Contactus()
                .contactname(DEFAULT_CONTACTNAME)
                .email(DEFAULT_EMAIL)
                .message(DEFAULT_MESSAGE);
        return contactus;
    }

    @Before
    public void initTest() {
        contactus = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactus() throws Exception {
        int databaseSizeBeforeCreate = contactusRepository.findAll().size();

        // Create the Contactus

        restContactusMockMvc.perform(post("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactus)))
            .andExpect(status().isCreated());

        // Validate the Contactus in the database
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeCreate + 1);
        Contactus testContactus = contactusList.get(contactusList.size() - 1);
        assertThat(testContactus.getContactname()).isEqualTo(DEFAULT_CONTACTNAME);
        assertThat(testContactus.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactus.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createContactusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactusRepository.findAll().size();

        // Create the Contactus with an existing ID
        Contactus existingContactus = new Contactus();
        existingContactus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactusMockMvc.perform(post("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingContactus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContactnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactusRepository.findAll().size();
        // set the field null
        contactus.setContactname(null);

        // Create the Contactus, which fails.

        restContactusMockMvc.perform(post("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactus)))
            .andExpect(status().isBadRequest());

        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactusRepository.findAll().size();
        // set the field null
        contactus.setEmail(null);

        // Create the Contactus, which fails.

        restContactusMockMvc.perform(post("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactus)))
            .andExpect(status().isBadRequest());

        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactusRepository.findAll().size();
        // set the field null
        contactus.setMessage(null);

        // Create the Contactus, which fails.

        restContactusMockMvc.perform(post("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactus)))
            .andExpect(status().isBadRequest());

        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactuses() throws Exception {
        // Initialize the database
        contactusRepository.saveAndFlush(contactus);

        // Get all the contactusList
        restContactusMockMvc.perform(get("/api/contactuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactus.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactname").value(hasItem(DEFAULT_CONTACTNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getContactus() throws Exception {
        // Initialize the database
        contactusRepository.saveAndFlush(contactus);

        // Get the contactus
        restContactusMockMvc.perform(get("/api/contactuses/{id}", contactus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactus.getId().intValue()))
            .andExpect(jsonPath("$.contactname").value(DEFAULT_CONTACTNAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactus() throws Exception {
        // Get the contactus
        restContactusMockMvc.perform(get("/api/contactuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactus() throws Exception {
        // Initialize the database
        contactusRepository.saveAndFlush(contactus);
        int databaseSizeBeforeUpdate = contactusRepository.findAll().size();

        // Update the contactus
        Contactus updatedContactus = contactusRepository.findOne(contactus.getId());
        updatedContactus
                .contactname(UPDATED_CONTACTNAME)
                .email(UPDATED_EMAIL)
                .message(UPDATED_MESSAGE);

        restContactusMockMvc.perform(put("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactus)))
            .andExpect(status().isOk());

        // Validate the Contactus in the database
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeUpdate);
        Contactus testContactus = contactusList.get(contactusList.size() - 1);
        assertThat(testContactus.getContactname()).isEqualTo(UPDATED_CONTACTNAME);
        assertThat(testContactus.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactus.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingContactus() throws Exception {
        int databaseSizeBeforeUpdate = contactusRepository.findAll().size();

        // Create the Contactus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContactusMockMvc.perform(put("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactus)))
            .andExpect(status().isCreated());

        // Validate the Contactus in the database
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContactus() throws Exception {
        // Initialize the database
        contactusRepository.saveAndFlush(contactus);
        int databaseSizeBeforeDelete = contactusRepository.findAll().size();

        // Get the contactus
        restContactusMockMvc.perform(delete("/api/contactuses/{id}", contactus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
