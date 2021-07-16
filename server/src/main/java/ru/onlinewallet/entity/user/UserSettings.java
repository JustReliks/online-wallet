package ru.onlinewallet.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "w_user_settings")
@PersistenceUnit(unitName = "postgres")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column
    private String about;

    @Column
    private String url;

    @Column
    private String phone;

    @Column
    private String country;

    @Column
    private String language;

    @Column
    private String currency;

}
