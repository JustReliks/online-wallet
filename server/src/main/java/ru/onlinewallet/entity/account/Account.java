package ru.onlinewallet.entity.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "w_accounts")
@PersistenceUnit(unitName = "postgres")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "last_transaction")
    private Instant lastTransaction;

    @Column
    private byte[] icon;

//    @OneToMany
//    @JoinTable(name = "w_account_bills",
//            joinColumns = @JoinColumn(name = "account_id"),
//            foreignKey = @ForeignKey(name = "w_account_bills_w_accounts_id_fk"))
//    private List<AccountBill> accountBills = new ArrayList<>();
}
