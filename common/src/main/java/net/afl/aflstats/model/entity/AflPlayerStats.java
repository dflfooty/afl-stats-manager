package net.afl.aflstats.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = { "fixture" })
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AflPlayerStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "afl_fixtures_id_gen", sequenceName = "afl_fixtures_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "afl_fixtures_id_gen")
    @Column(updatable = false)
    private long id;

    private int round;
    private String name;
    String team;
    private int jumperNo;
    private int kicks;
    private int handballs;
    private int disposals;
    private int marks;
    private int hitouts;
    private int freesFor;
    private int freesAgainst;
    private int tackles;
    private int goals;
    private int behinds;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    private AflFixture fixture;

    public boolean equalsLogically(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AflPlayerStats other = (AflPlayerStats) obj;
        if (behinds != other.behinds)
            return false;
        if (disposals != other.disposals)
            return false;
        if (freesAgainst != other.freesAgainst)
            return false;
        if (freesFor != other.freesFor)
            return false;
        if (goals != other.goals)
            return false;
        if (handballs != other.handballs)
            return false;
        if (hitouts != other.hitouts)
            return false;
        if (jumperNo != other.jumperNo)
            return false;
        if (kicks != other.kicks)
            return false;
        if (marks != other.marks)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (round != other.round)
            return false;
        if (tackles != other.tackles)
            return false;
        if (team == null) {
            if (other.team != null)
                return false;
        } else if (!team.equals(other.team))
            return false;
        return true;
    }

}
