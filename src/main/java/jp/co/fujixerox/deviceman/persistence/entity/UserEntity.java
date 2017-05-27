package jp.co.fujixerox.deviceman.persistence.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USERS")
@Getter
@ToString
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID of the user.
     */
    @Id
    @Column(name = "ID")
    @Pattern(regexp = "[!-~]+") // ascii
    private String id;

    /**
     * Address of the user.
     */
    @Column(name = "ADDRESS", nullable = false)
    @Pattern(regexp = "[a-z0-9\\+\\-_]+(\\.[a-z0-9\\+\\-_]+)*@([a-z0-9\\-]+\\.)+[a-z]{2,6}")
    @Setter
    private String address;

    /**
     * password of the user.
     */
    @Column(name = "PASSWORD", nullable = false)
    @Size(min = 4)
    @Pattern(regexp = "[!-~]+") // ascii
    @Setter
    private String password;

    /**
     * Date that the user created.
     */
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * Date that the user updated.
     */
    @Column(name = "UPDATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    protected UserEntity() {
    }

    public UserEntity(
            @NonNull String id,
            @NonNull String address,
            @NonNull String password) {
        this.id = id;
        this.address = address;
        this.password = password;
    }

    @PrePersist
    public void onPersist() {
        created = new Date();
        updated = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        updated = new Date();
    }
}
