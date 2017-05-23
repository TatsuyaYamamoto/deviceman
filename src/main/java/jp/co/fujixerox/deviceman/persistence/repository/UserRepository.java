package jp.co.fujixerox.deviceman.persistence.repository;

import jp.co.fujixerox.deviceman.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
    @Query("SELECT u FROM UserEntity u WHERE (CONCAT(u.id, u.address)) LIKE %:query%")
    List<UserEntity> search(@Param("query") String query);
}
