package ru.onlinewallet.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinewallet.entity.user.User;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
@Entity
@Table(name = "restore_access_tokens")
@NoArgsConstructor
@AllArgsConstructor
public class RestoreAccessToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public RestoreAccessToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = Date.from(Instant.now().plus(2, ChronoUnit.HOURS));
    }
}
