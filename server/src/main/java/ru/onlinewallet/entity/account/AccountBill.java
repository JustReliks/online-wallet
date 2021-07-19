package ru.onlinewallet.entity.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "w_account_bills")
@PersistenceUnit(unitName = "postgres")
public class AccountBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column
    private Double balance;
}
