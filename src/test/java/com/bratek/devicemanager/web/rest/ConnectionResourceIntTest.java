package com.bratek.devicemanager.web.rest;

import com.bratek.devicemanager.DeviceManagerApp;
import com.bratek.devicemanager.domain.Connection;
import com.bratek.devicemanager.repository.ConnectionRepository;
import com.bratek.devicemanager.repository.UserRepository;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ConnectionResource REST controller.
 *
 * @see ConnectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeviceManagerApp.class)
public class ConnectionResourceIntTest {

    private static final String DEFAULT_USERHOST = "AAAAAAAAAA";
    private static final String UPDATED_USERHOST = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Inject
    private ConnectionRepository connectionRepository;

    @Inject
    private UserRepository userRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    @Inject
    private WebApplicationContext context;

    private MockMvc restConnectionMockMvc;

    private Connection connection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            ConnectionResource connectionResource = new ConnectionResource(connectionRepository);

        ReflectionTestUtils.setField(connectionResource, "connectionRepository", connectionRepository);
        ReflectionTestUtils.setField(connectionResource, "userRepository", userRepository);

        this.restConnectionMockMvc = MockMvcBuilders.standaloneSetup(connectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();


    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Connection createEntity(EntityManager em) {
        Connection connection = new Connection()
                .userhost(DEFAULT_USERHOST)
                .password(DEFAULT_PASSWORD);
        return connection;
    }

    @Before
    public void initTest() {
        connection = createEntity(em);
    }

    @Test
    @Transactional
    public void createConnection() throws Exception {
        int databaseSizeBeforeCreate = connectionRepository.findAll().size();

        restConnectionMockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();


        // Create the Connection

        restConnectionMockMvc.perform(post("/api/connections")
            .with(user("user").roles("USER"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isCreated());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeCreate + 1);
        Connection testConnection = connectionList.get(connectionList.size() - 1);
        assertThat(testConnection.getUserhost()).isEqualTo(DEFAULT_USERHOST);
        assertThat(testConnection.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createConnectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = connectionRepository.findAll().size();

        // Create the Connection with an existing ID
        Connection existingConnection = new Connection();
        existingConnection.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConnectionMockMvc.perform(post("/api/connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingConnection)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserhostIsRequired() throws Exception {
        int databaseSizeBeforeTest = connectionRepository.findAll().size();
        // set the field null
        connection.setUserhost(null);

        // Create the Connection, which fails.

        restConnectionMockMvc.perform(post("/api/connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isBadRequest());

        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = connectionRepository.findAll().size();
        // set the field null
        connection.setPassword(null);

        // Create the Connection, which fails.

        restConnectionMockMvc.perform(post("/api/connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isBadRequest());

        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConnections() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);

        //Create security-aware mockMvc
        restConnectionMockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        // Get all the connectionList
        restConnectionMockMvc.perform(get("/api/connections?sort=id,desc")
            .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk());

    }

    @Test
    @Transactional
    public void getConnection() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);

        // Get the connection
        restConnectionMockMvc.perform(get("/api/connections/{id}", connection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(connection.getId().intValue()))
            .andExpect(jsonPath("$.userhost").value(DEFAULT_USERHOST.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConnection() throws Exception {
        // Get the connection
        restConnectionMockMvc.perform(get("/api/connections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConnection() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);
        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();

        // Update the connection
        Connection updatedConnection = connectionRepository.findOne(connection.getId());
        updatedConnection
                .userhost(UPDATED_USERHOST)
                .password(UPDATED_PASSWORD);

        restConnectionMockMvc.perform(put("/api/connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConnection)))
            .andExpect(status().isOk());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
        Connection testConnection = connectionList.get(connectionList.size() - 1);
        assertThat(testConnection.getUserhost()).isEqualTo(UPDATED_USERHOST);
        assertThat(testConnection.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void updateNonExistingConnection() throws Exception {
        int databaseSizeBeforeUpdate = connectionRepository.findAll().size();


        restConnectionMockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        // Create the Connection

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConnectionMockMvc.perform(put("/api/connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connection)))
            .andExpect(status().isCreated());

        // Validate the Connection in the database
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConnection() throws Exception {
        // Initialize the database
        connectionRepository.saveAndFlush(connection);
        int databaseSizeBeforeDelete = connectionRepository.findAll().size();

        // Get the connection
        restConnectionMockMvc.perform(delete("/api/connections/{id}", connection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Connection> connectionList = connectionRepository.findAll();
        assertThat(connectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Connection.class);
    }
}
