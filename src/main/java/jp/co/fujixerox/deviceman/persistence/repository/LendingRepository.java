package jp.co.fujixerox.deviceman.persistence.repository;

import jp.co.fujixerox.deviceman.persistence.entity.LendingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LendingRepository extends JpaRepository<LendingEntity, Integer>, JpaSpecificationExecutor<LendingEntity> {
    /**
     * Find active, someone is lending, Lending entity with {@link LendingEntity#deviceId}.
     *
     * @param deviceId
     * @return
     */
    @Query("SELECT l FROM LendingEntity l WHERE l.deviceId = :deviceId AND l.actualReturnDate IS NULL")
    LendingEntity findActiveLendingByDeviceId(@Param("deviceId") Integer deviceId);
}
