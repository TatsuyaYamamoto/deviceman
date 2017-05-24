package jp.co.fujixerox.deviceman.persistence.repository;

import jp.co.fujixerox.deviceman.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Integer>, JpaSpecificationExecutor<DeviceEntity> {
    /**
     * Search with query.
     *
     * @param query
     * @return
     */
    @Query("SELECT d " +
            "FROM DeviceEntity d " +
            "WHERE (CONCAT(d.name, d.manufacturer, d.osName,osVersion, d.imei, d.wifiMacAddress)) " +
            "LIKE %:query%")
    List<DeviceEntity> search(@Param("query") String query);

    /**
     * Check existence with {@link DeviceEntity#imei}.
     *
     * @param imei
     * @return
     */
    boolean existsByImei(String imei);

    /**
     * Check existence with {@link DeviceEntity#wifiMacAddress}.
     *
     * @param wifiMacAddress
     * @return
     */
    boolean existsByWifiMacAddress(String wifiMacAddress);
}
