package com.bratek.devicemanager.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DiscLog.
 */
@Entity
@Table(name = "disc_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DiscLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "util", nullable = false)
    private Double util;

    @NotNull
    @Column(name = "svctim", nullable = false)
    private Double svctim;

    @NotNull
    @Column(name = "await", nullable = false)
    private Double await;

    @NotNull
    @Column(name = "avgqusz", nullable = false)
    private Double avgqusz;

    @NotNull
    @Column(name = "avgrqsz", nullable = false)
    private Double avgrqsz;

    @Column(name = "date")
    private ZonedDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Disc disc;

    public Disc getDisc() {
        return disc;
    }

    public void setDisc(Disc disc) {
        this.disc = disc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getUtil() {
        return util;
    }

    public DiscLog util(Double util) {
        this.util = util;
        return this;
    }

    public void setUtil(Double util) {
        this.util = util;
    }

    public Double getSvctim() {
        return svctim;
    }

    public DiscLog svctim(Double svctim) {
        this.svctim = svctim;
        return this;
    }

    public void setSvctim(Double svctim) {
        this.svctim = svctim;
    }

    public Double getAwait() {
        return await;
    }

    public DiscLog await(Double await) {
        this.await = await;
        return this;
    }

    public void setAwait(Double await) {
        this.await = await;
    }

    public Double getAvgqusz() {
        return avgqusz;
    }

    public DiscLog avgqusz(Double avgqusz) {
        this.avgqusz = avgqusz;
        return this;
    }

    public void setAvgqusz(Double avgqusz) {
        this.avgqusz = avgqusz;
    }

    public Double getAvgrqsz() {
        return avgrqsz;
    }

    public DiscLog avgrqsz(Double avgrqsz) {
        this.avgrqsz = avgrqsz;
        return this;
    }

    public void setAvgrqsz(Double avgrqsz) {
        this.avgrqsz = avgrqsz;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public DiscLog date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscLog discLog = (DiscLog) o;
        if (discLog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, discLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DiscLog{" +
            "id=" + id +
            ", util='" + util + "'" +
            ", svctim='" + svctim + "'" +
            ", await='" + await + "'" +
            ", avgqusz='" + avgqusz + "'" +
            ", avgrqsz='" + avgrqsz + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
