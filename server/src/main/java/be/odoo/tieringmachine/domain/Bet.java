package be.odoo.tieringmachine.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bet_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Citizen user;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Citizen creator;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Citizen target;

    @Column(name = "wager_points")
    private int wagerPoints;

    @Column(name = "bet_details", length = 100)
    private String betDetails;

    @Column(name = "actual_outcome")
    private Boolean actualOutcome;

    @Column(name = "payout_points")
    private Integer payoutPoints;

    @Column(name = "loss_points")
    private Integer lossPoints;

    @Column(name = "resolution_date")
    private LocalDate resolutionDate;
}
