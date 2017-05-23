package jp.co.fujixerox.deviceman.service;

import jp.co.fujixerox.deviceman.persistence.entity.UserEntity;
import jp.co.fujixerox.deviceman.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Search users with query.
     *
     * @param query searching query. target are id or address.
     * @return
     */
    public List<UserEntity> search(String query) {
        return repository.search(query);
    }

    /**
     * Register new user entity.
     *
     * @param id      ID of the new user.
     * @param address Address of the new user.
     * @return the saved entity. {@link CrudRepository#save(Object)}
     * @throws ConflictException when provided ID is already registered.
     */
    @Transactional(readOnly = false)
    public UserEntity create(
            String id,
            String address) throws ConflictException {
        if (repository.exists(id)) {
            throw new ConflictException(String.format("cannot register as a result of to conflict (ID: %s)", id));
        }

        UserEntity newUserEntity = new UserEntity(
                id,
                address,
                passwordEncoder.encode(id));

        return repository.save(newUserEntity);
    }
}
