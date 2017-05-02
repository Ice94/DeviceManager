package com.bratek.devicemanager.web.rest;

import com.bratek.devicemanager.DeviceManagerApp;

import com.bratek.devicemanager.domain.DiscLog;
import com.bratek.devicemanager.repository.DiscLogRepository;

import com.bratek.devicemanager.repository.DiscRepository;
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
 * Test class for the DiscLogResource REST controller.
 *
 * @see DiscLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeviceManagerApp.class)
public class DiscLogResourceIntTest {

    private static final Double DEFAULT_UTIL = 1D;
    private static final Double UPDATED_UTIL = 2D;

    private static final Double DEFAULT_SVCTIM = 1D;
    private static final Double UPDATED_SVCTIM = 2D;

    private static final Double DEFAULT_AWAIT = 1D;
    private static final Double UPDATED_AWAIT = 2D;

    private static final Double DEFAULT_AVGQUSZ = 1D;
    private static final Double UPDATED_AVGQUSZ = 2D;

    private static final Double DEFAULT_AVGRQSZ = 1D;
    private static final Double UPDATED_AVGRQSZ = 2D;

    @Autowired
    private DiscLogRepository discLogRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    @Autowired
    private DiscRepository discRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc restDiscLogMockMvc;

    private DiscLog discLog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            DiscLogResource discLogResource = new DiscLogResource(discLogRepository);
        this.restDiscLogMockMvc = MockMvcBuilders.standaloneSetup(discLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscLog createEntity(EntityManager em) {
        DiscLog discLog = new DiscLog()
                .util(DEFAULT_UTIL)
                .svctim(DEFAULT_SVCTIM)
                .await(DEFAULT_AWAIT)
                .avgqusz(DEFAULT_AVGQUSZ)
                .avgrqsz(DEFAULT_AVGRQSZ);
        return discLog;
    }

    @Before
    public void initTest() {
        discLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscLog() throws Exception {
        int databaseSizeBeforeCreate = discLogRepository.findAll().size();

        restDiscLogMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();


        // Create the DiscLog

        restDiscLogMockMvc.perform(post("/api/disc-logs")
            .with(user("user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discLog)))
            .andExpect(status().isCreated());

        // Validate the DiscLog in the database
        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeCreate + 1);
        DiscLog testDiscLog = discLogList.get(discLogList.size() - 1);
        assertThat(testDiscLog.getUtil()).isEqualTo(DEFAULT_UTIL);
        assertThat(testDiscLog.getSvctim()).isEqualTo(DEFAULT_SVCTIM);
        assertThat(testDiscLog.getAwait()).isEqualTo(DEFAULT_AWAIT);
        assertThat(testDiscLog.getAvgqusz()).isEqualTo(DEFAULT_AVGQUSZ);
        assertThat(testDiscLog.getAvgrqsz()).isEqualTo(DEFAULT_AVGRQSZ);
    }

    @Test
    @Transactional
    public void createDiscLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discLogRepository.findAll().size();

        // Create the DiscLog with an existing ID
        DiscLog existingDiscLog = new DiscLog();
        existingDiscLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscLogMockMvc.perform(post("/api/disc-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDiscLog)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUtilIsRequired() throws Exception {
        int databaseSizeBeforeTest = discLogRepository.findAll().size();
        // set the field null
        discLog.setUtil(null);

        // Create the DiscLog, which fails.

        restDiscLogMockMvc.perform(post("/api/disc-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discLog)))
            .andExpect(status().isBadRequest());

        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSvctimIsRequired() throws Exception {
        int databaseSizeBeforeTest = discLogRepository.findAll().size();
        // set the field null
        discLog.setSvctim(null);

        // Create the DiscLog, which fails.

        restDiscLogMockMvc.perform(post("/api/disc-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discLog)))
            .andExpect(status().isBadRequest());

        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAwaitIsRequired() throws Exception {
        int databaseSizeBeforeTest = discLogRepository.findAll().size();
        // set the field null
        discLog.setAwait(null);

        // Create the DiscLog, which fails.

        restDiscLogMockMvc.perform(post("/api/disc-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discLog)))
            .andExpect(status().isBadRequest());

        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAvgquszIsRequired() throws Exception {
        int databaseSizeBeforeTest = discLogRepository.findAll().size();
        // set the field null
        discLog.setAvgqusz(null);

        // Create the DiscLog, which fails.

        restDiscLogMockMvc.perform(post("/api/disc-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discLog)))
            .andExpect(status().isBadRequest());

        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAvgrqszIsRequired() throws Exception {
        int databaseSizeBeforeTest = discLogRepository.findAll().size();
        // set the field null
        discLog.setAvgrqsz(null);

        // Create the DiscLog, which fails.

        restDiscLogMockMvc.perform(post("/api/disc-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discLog)))
            .andExpect(status().isBadRequest());

        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscLogs() throws Exception {
        // Initialize the database
        discLogRepository.saveAndFlush(discLog);

        restDiscLogMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        // Get all the discLogList
        restDiscLogMockMvc.perform(get("/api/disc-logs?sort=id,desc")
            .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].util").value(hasItem(DEFAULT_UTIL.doubleValue())))
            .andExpect(jsonPath("$.[*].svctim").value(hasItem(DEFAULT_SVCTIM.doubleValue())))
            .andExpect(jsonPath("$.[*].await").value(hasItem(DEFAULT_AWAIT.doubleValue())))
            .andExpect(jsonPath("$.[*].avgqusz").value(hasItem(DEFAULT_AVGQUSZ.doubleValue())))
            .andExpect(jsonPath("$.[*].avgrqsz").value(hasItem(DEFAULT_AVGRQSZ.doubleValue())));
    }

    @Test
    @Transactional
    public void getDiscLog() throws Exception {
        // Initialize the database
        discLogRepository.saveAndFlush(discLog);

        // Get the discLog
        restDiscLogMockMvc.perform(get("/api/disc-logs/{id}", discLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discLog.getId().intValue()))
            .andExpect(jsonPath("$.util").value(DEFAULT_UTIL.doubleValue()))
            .andExpect(jsonPath("$.svctim").value(DEFAULT_SVCTIM.doubleValue()))
            .andExpect(jsonPath("$.await").value(DEFAULT_AWAIT.doubleValue()))
            .andExpect(jsonPath("$.avgqusz").value(DEFAULT_AVGQUSZ.doubleValue()))
            .andExpect(jsonPath("$.avgrqsz").value(DEFAULT_AVGRQSZ.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscLog() throws Exception {
        // Get the discLog
        restDiscLogMockMvc.perform(get("/api/disc-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscLog() throws Exception {
        // Initialize the database
        discLogRepository.saveAndFlush(discLog);
        int databaseSizeBeforeUpdate = discLogRepository.findAll().size();

        // Update the discLog
        DiscLog updatedDiscLog = discLogRepository.findOne(discLog.getId());
        updatedDiscLog
                .util(UPDATED_UTIL)
                .svctim(UPDATED_SVCTIM)
                .await(UPDATED_AWAIT)
                .avgqusz(UPDATED_AVGQUSZ)
                .avgrqsz(UPDATED_AVGRQSZ);

        restDiscLogMockMvc.perform(put("/api/disc-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscLog)))
            .andExpect(status().isOk());

        // Validate the DiscLog in the database
        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeUpdate);
        DiscLog testDiscLog = discLogList.get(discLogList.size() - 1);
        assertThat(testDiscLog.getUtil()).isEqualTo(UPDATED_UTIL);
        assertThat(testDiscLog.getSvctim()).isEqualTo(UPDATED_SVCTIM);
        assertThat(testDiscLog.getAwait()).isEqualTo(UPDATED_AWAIT);
        assertThat(testDiscLog.getAvgqusz()).isEqualTo(UPDATED_AVGQUSZ);
        assertThat(testDiscLog.getAvgrqsz()).isEqualTo(UPDATED_AVGRQSZ);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscLog() throws Exception {
        int databaseSizeBeforeUpdate = discLogRepository.findAll().size();

        // Create the DiscLog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDiscLogMockMvc.perform(put("/api/disc-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discLog)))
            .andExpect(status().isCreated());

        // Validate the DiscLog in the database
        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDiscLog() throws Exception {
        // Initialize the database
        discLogRepository.saveAndFlush(discLog);
        int databaseSizeBeforeDelete = discLogRepository.findAll().size();

        // Get the discLog
        restDiscLogMockMvc.perform(delete("/api/disc-logs/{id}", discLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DiscLog> discLogList = discLogRepository.findAll();
        assertThat(discLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscLog.class);
    }
}
