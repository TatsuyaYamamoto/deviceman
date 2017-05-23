package jp.co.fujixerox.deviceman.persistence.repository;

import jp.co.fujixerox.deviceman.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Integer>, JpaSpecificationExecutor<DeviceEntity> {
}
