package ru.onlinewallet.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "w_users")
@PersistenceUnit(unitName = "postgres")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @ManyToMany
    @JoinTable(name = "w_users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @JsonManagedReference
    private Set<Role> roles;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private Double balance;

    @Column
    private String uuid;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "jwt_created_time_hash")
    private String jwtCreatedTimeHash;

    @Column(name = "enable_two_factor_auth")
    private Boolean isTwoFactorEnabled;
}
