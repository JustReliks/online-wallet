package ru.onlinewallet.entity.security;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "w_users_totp")
@PersistenceUnit(unitName = "postgres")
@TypeDef(
        name = "int-array",
        typeClass = IntArrayType.class
)
public class UserTOTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "validation_code")
    private Integer validationCode;

    @Type(type = "int-array")
    @Column(
            name = "scratch_codes",
            columnDefinition = "bigint[]"
    )
    private int[] scratchCodes;

    @Column(name = "need_regenerate")
    private Boolean needRegenerate;
}
