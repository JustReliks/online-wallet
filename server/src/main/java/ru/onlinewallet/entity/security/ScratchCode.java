package ru.onlinewallet.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scratch_code")
@PersistenceUnit(unitName = "postgres")
public class ScratchCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_totp_id", referencedColumnName = "id")
    private UserTOTP userTOTP;

    @Column
    private Integer code;
}
