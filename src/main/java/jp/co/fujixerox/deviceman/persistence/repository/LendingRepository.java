package jp.co.fujixerox.deviceman.persistence.repository;

import jp.co.fujixerox.deviceman.persistence.entity.LendingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LendingRepository extends JpaRepository<LendingEntity, Integer>, JpaSpecificationExecutor<LendingEntity> {
    /**
     * Find with {@link LendingEntity#deviceId}.
     *
     * @param deviceId
     * @return
     */
    @Query("SELECT l FROM LendingEntity l WHERE l.actualReturnDate IS NOT NULL")
    LendingEntity findActiveLendingByDeviceId(Integer deviceId);
}
