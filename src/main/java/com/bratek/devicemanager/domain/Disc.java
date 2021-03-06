package com.bratek.devicemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Disc.
 */
@Entity
@Table(name = "disc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Disc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private Connection connection;

    @OneToMany(mappedBy = "disc", fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DiscLog> discLogs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Disc name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public Disc connection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Set<DiscLog> getDiscLogs() {
        return discLogs;
    }

    public Disc discLogs(Set<DiscLog> discLogs) {
        this.discLogs = discLogs;
        return this;
    }

    public Disc addDiscLog(DiscLog discLog) {
        this.discLogs.add(discLog);
        discLog.setDisc(this);
        return this;
    }

    public Disc removeDiscLog(DiscLog discLog) {
        this.discLogs.remove(discLog);
        discLog.setDisc(null);
        return this;
    }

    public void setDiscLogs(Set<DiscLog> discLogs) {
        this.discLogs = discLogs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Disc disc = (Disc) o;
        if (disc.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, disc.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Disc{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
