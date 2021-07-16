package ru.onlinewallet.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "w_roles")
@PersistenceUnit(unitName = "postgres")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    //@ManyToMany(mappedBy = "roles")
    //private Set<User> users;
}
