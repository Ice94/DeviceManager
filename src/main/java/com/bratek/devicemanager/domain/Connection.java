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
 * A Connection.
 */
@Entity
@Table(name = "connection")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Connection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "userhost", nullable = false)
    private String userhost;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "connection")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Disc> connections = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserhost() {
        return userhost;
    }

    public Connection userhost(String userhost) {
        this.userhost = userhost;
        return this;
    }

    public void setUserhost(String userhost) {
        this.userhost = userhost;
    }

    public String getPassword() {
        return password;
    }

    public Connection password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Disc> getConnections() {
        return connections;
    }

    public Connection connections(Set<Disc> discs) {
        this.connections = discs;
        return this;
    }

    public Connection addConnections(Disc disc) {
        this.connections.add(disc);
        disc.setConnection(this);
        return this;
    }

    public Connection removeConnections(Disc disc) {
        this.connections.remove(disc);
        disc.setConnection(null);
        return this;
    }

    public void setConnections(Set<Disc> discs) {
        this.connections = discs;
    }

    public User getUser() {
        return user;
    }

    public Connection user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Connection connection = (Connection) o;
        if (connection.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, connection.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Connection{" +
            "id=" + id +
            ", userhost='" + userhost + "'" +
            ", password='" + password + "'" +
            '}';
    }
}
