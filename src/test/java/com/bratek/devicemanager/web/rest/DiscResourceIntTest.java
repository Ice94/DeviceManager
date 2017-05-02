package com.bratek.devicemanager.web.rest;

import com.bratek.devicemanager.DeviceManagerApp;

import com.bratek.devicemanager.domain.Disc;
import com.bratek.devicemanager.repository.ConnectionRepository;
import com.bratek.devicemanager.repository.DiscRepository;

import com.bratek.devicemanager.security.AuthoritiesConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DiscResource REST controller.
 *
 * @see DiscResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeviceManagerApp.class)
public class DiscResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DiscRepository discRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc restDiscMockMvc;

    private Disc disc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            DiscResource discResource = new DiscResource(discRepository);
        this.restDiscMockMvc = MockMvcBuilders.standaloneSetup(discResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disc createEntity(EntityManager em) {
        Disc disc = new Disc()
                .name(DEFAULT_NAME);
        return disc;
    }

    @Before
    public void initTest() {
        disc = createEntity(em);
    }

    @Test
    @Transactional
    public void createDisc() throws Exception {
        int databaseSizeBeforeCreate = discRepository.findAll().size();

        // Create security-aware mock
        restDiscMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        // Create the Disc

        restDiscMockMvc.perform(post("/api/discs")
            .with(user(AuthoritiesConstants.USER))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disc)))
            .andExpect(status().isCreated());

        // Validate the Disc in the database
        List<Disc> discList = discRepository.findAll();
        assertThat(discList).hasSize(databaseSizeBeforeCreate + 1);
        Disc testDisc = discList.get(discList.size() - 1);
        assertThat(testDisc.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDiscWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discRepository.findAll().size();

        // Create the Disc with an existing ID
        Disc existingDisc = new Disc();
        existingDisc.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscMockMvc.perform(post("/api/discs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDisc)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Disc> discList = discRepository.findAll();
        assertThat(discList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = discRepository.findAll().size();
        // set the field null
        disc.setName(null);

        // Create the Disc, which fails.

        restDiscMockMvc.perform(post("/api/discs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disc)))
            .andExpect(status().isBadRequest());

        List<Disc> discList = discRepository.findAll();
        assertThat(discList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscs() throws Exception {
        // Initialize the database
        discRepository.saveAndFlush(disc);

        // Create security-aware mock
        restDiscMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();


        // Get all the discList
        restDiscMockMvc.perform(get("/api/discs?sort=id,desc")
            .with(user(AuthoritiesConstants.ADMIN).roles(AuthoritiesConstants.ADMIN)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disc.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDisc() throws Exception {
        // Initialize the database
        discRepository.saveAndFlush(disc);

        // Get the disc
        restDiscMockMvc.perform(get("/api/discs/{id}", disc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(disc.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDisc() throws Exception {
        // Get the disc
        restDiscMockMvc.perform(get("/api/discs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisc() throws Exception {
        // Initialize the database
        discRepository.saveAndFlush(disc);
        int databaseSizeBeforeUpdate = discRepository.findAll().size();

        // Update the disc
        Disc updatedDisc = discRepository.findOne(disc.getId());
        updatedDisc
                .name(UPDATED_NAME);

        restDiscMockMvc.perform(put("/api/discs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDisc)))
            .andExpect(status().isOk());

        // Validate the Disc in the database
        List<Disc> discList = discRepository.findAll();
        assertThat(discList).hasSize(databaseSizeBeforeUpdate);
        Disc testDisc = discList.get(discList.size() - 1);
        assertThat(testDisc.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDisc() throws Exception {
        int databaseSizeBeforeUpdate = discRepository.findAll().size();

        // Create the Disc

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDiscMockMvc.perform(put("/api/discs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disc)))
            .andExpect(status().isCreated());

        // Validate the Disc in the database
        List<Disc> discList = discRepository.findAll();
        assertThat(discList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDisc() throws Exception {
        // Initialize the database
        discRepository.saveAndFlush(disc);
        int databaseSizeBeforeDelete = discRepository.findAll().size();

        // Get the disc
        restDiscMockMvc.perform(delete("/api/discs/{id}", disc.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Disc> discList = discRepository.findAll();
        assertThat(discList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disc.class);
    }
}
