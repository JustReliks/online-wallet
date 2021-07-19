package ru.onlinewallet.entity.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "w_currencies")
@PersistenceUnit(unitName = "postgres")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "long_name")
    private String longName;
}
