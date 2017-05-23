package jp.co.fujixerox.deviceman.persistence.entity;

import jp.co.fujixerox.deviceman.persistence.entity.type.OsName;
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
@Table(name = "DEVICES")
@Getter
@ToString
public class DeviceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Name of the device.
     */
    @Column(name = "NAME", nullable = false)
    @Size(max = 60)
    private String name;

    /**
     * Manufacturer of the device.
     */
    @Column(name = "MANUFACTURER", nullable = false)
    @Size(max = 60)
    private String manufacturer;

    /**
     * OS name of the device.
     */
    @Column(name = "OS_NAME", nullable = false)
    private OsName osName;

    /**
     * OS version of the device.
     */
    @Column(name = "OS_VERSION", nullable = false)
    @Pattern(regexp = "[0-9]+(\\.[0-9]+){0,2}") // Semantic Versioning
    @Setter
    private String osVersion;

    /**
     * IMEI of the device.
     */
    @Column(name = "IMEI", unique = true)
    @Pattern(regexp = "[0-9]{15}")
    private String imei;

    /**
     * Wi-Fi Mac address of the device.
     */
    @Column(name = "WIFI_MAC_ADDRESS", nullable = false, unique = true)
    @Pattern(regexp = "([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}")
    private String wifiMacAddress;

    /**
     * Phone numberOS of the device.
     */
    @Column(name = "PHONE_NUMBER", nullable = false)
    @Pattern(regexp = "[0-9]{11}")
    @Setter
    private String phoneNumber;

    /**
     * Date that the device created.
     */
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * Date that the device updated.
     */
    @Column(name = "UPDATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    protected DeviceEntity() {
    }

    public DeviceEntity(
            @NonNull String name,
            @NonNull String manufacturer,
            @NonNull OsName osName,
            @NonNull String osVersion,
            @NonNull String imei,
            @NonNull String wifiMacAddress) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.osName = osName;
        this.wifiMacAddress = osVersion;
        this.imei = imei;
        this.wifiMacAddress = wifiMacAddress;
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

    /******************************************************************
     * Relation ship
     */
    @OneToOne(mappedBy = "device")
    private LendingEntity checkout;

}
