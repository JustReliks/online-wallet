package ru.onlinewallet.entity.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@Table(name = "w_goal")
@PersistenceUnit(unitName = "postgres")
public class AccountGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column
    private String name;

    @Column
    private Double value;

    @Column
    private Instant date;

    @Column
    private boolean completed;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Account account;


}
