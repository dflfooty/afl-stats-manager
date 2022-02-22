package net.afl.aflstats.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = { "homeTeamStats", "awayTeamStats" })
@NoArgsConstructor
@Entity
@Table(name = "afl_fixtures")
public class AflFixture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "afl_fixtures_id_gen", sequenceName = "afl_fixtures_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "afl_fixtures_id_gen")
    @Column(updatable = false)
    private long id;

    private int round;
    private int game;
    private String homeTeam;
    private String awayTeam;
    private String ground;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime statsLastDownload;

    @ToString.Exclude
    @OneToMany
    @JoinColumns({ @JoinColumn(name = "fixture_id", referencedColumnName = "id"),
            @JoinColumn(name = "team", referencedColumnName = "homeTeam"), })
    private List<AflPlayerStats> homeTeamStats;

    @ToString.Exclude
    @OneToMany
    @JoinColumns({ @JoinColumn(name = "fixture_id", referencedColumnName = "id"),
            @JoinColumn(name = "team", referencedColumnName = "awayTeam"), })

    private List<AflPlayerStats> awayTeamStats;

}
