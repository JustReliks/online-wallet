package ru.onlinewallet.entity.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@Table(name = "w_transactions")
@PersistenceUnit(unitName = "postgres")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "bill_id")
    private Long billId;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "bill_id", referencedColumnName = "id", insertable = false, updatable = false)
    private AccountBill accountBill;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "datetime")
    private Instant dateTime;

    @Column
    private Double quantity;

    @OneToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private TransactionCategory category;
}
