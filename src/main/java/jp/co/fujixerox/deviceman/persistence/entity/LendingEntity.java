package jp.co.fujixerox.deviceman.persistence.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "LENDINGS")
@Getter
@ToString
public class LendingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /**
     * ID of the borrower.
     */
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    /**
     * ID of the checkouted device.
     */
    @Column(name = "DEVICE_ID", nullable = false)
    private Integer deviceId;

    /**
     * Date that it starts to be lent.
     */
    @Column(name = "LENDING_START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lendingStartDate;

    /**
     * Date that it will be returned.
     */
    @Column(name = "DUE_RETURN_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueReturnDate;

    /**
     * Date that it's returned.
     */
    @Column(name = "ACTUAL_RETURN_DATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private Date actualReturnDate;

    protected LendingEntity() {

    }

    public LendingEntity(
            @NonNull UserEntity user,
            @NonNull DeviceEntity device,
            @NonNull Date dueReturnDate) {

        this.userId = user.getId();
        this.deviceId = device.getId();
        this.dueReturnDate = dueReturnDate;
    }

    @PrePersist
    public void onPersist() {
        lendingStartDate = new Date();
    }

    /******************************************************************
     * Relation ship
     */
    @JoinColumn(name = "DEVICE_ID", insertable = false, updatable = false)
    @ManyToOne
    private DeviceEntity device;

    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    @ManyToOne
    private UserEntity user;
}
