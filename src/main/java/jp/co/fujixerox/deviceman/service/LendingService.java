package jp.co.fujixerox.deviceman.service;

import jp.co.fujixerox.deviceman.persistence.entity.DeviceEntity;
import jp.co.fujixerox.deviceman.persistence.entity.LendingEntity;
import jp.co.fujixerox.deviceman.persistence.entity.UserEntity;
import jp.co.fujixerox.deviceman.persistence.repository.DeviceRepository;
import jp.co.fujixerox.deviceman.persistence.repository.LendingRepository;
import jp.co.fujixerox.deviceman.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Service
@Transactional(readOnly = true)
public class LendingService {
    private static final int MAX_BORROWING_DAYS = 7;

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final LendingRepository lendingRepository;

    @Autowired
    public LendingService(
            UserRepository userRepository,
            DeviceRepository deviceRepository,
            LendingRepository lendingRepository) {

        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.lendingRepository = lendingRepository;
    }

    /**
     * 貸し出し処理を実行する
     *
     * @param deviceId      貸し出し要求をするユーザーID
     * @param userId        貸し出しを行う端末ID
     * @param dueReturnDate 返却予定日
     * @throws EntityNotFoundException  要求されたユーザー、または端末がない
     * @throws ConflictException        端末が貸し出し中
     * @throws IllegalArgumentException 貸出予定日が不正
     */
    @Transactional(readOnly = false)
    public LendingEntity applyLending(
            Integer deviceId,
            String userId,
            Date dueReturnDate) throws EntityNotFoundException, ConflictException, IllegalArgumentException {

        UserEntity requestUser = userRepository.getOne(userId);
        DeviceEntity requestDevice = deviceRepository.getOne(deviceId);

        /* 貸出状態の確認 */
        if (lendingRepository.findActiveLendingByDeviceId(deviceId) != null) {
            throw new ConflictException(String.format(
                    "Couldn't apply. Because the device(ID: %s) is lent.",
                    deviceId));
        }

        /* 貸出期間の確認 */
        long returnTimeMills = dueReturnDate.getTime();
        long minTimeMillis = currentTimeMillis();
        long maxTimeMillis = currentTimeMillis() + MAX_BORROWING_DAYS * 24 * 60 * 60 * 1000;

        if (returnTimeMills < minTimeMillis || maxTimeMillis < returnTimeMills) {
            throw new IllegalArgumentException(String.format(
                    "Couldn't apply lending. Because dueReturnDate must between %s and %s.",
                    minTimeMillis,
                    maxTimeMillis));
        }

        return lendingRepository.save(new LendingEntity(
                requestUser,
                requestDevice,
                dueReturnDate));
    }

    /**
     * 返却処理を実行する
     *
     * @throws EntityNotFoundException 要求された端末は貸し出されていない
     */
    @Transactional(readOnly = false)
    public void applyReturn(Integer deviceId) throws EntityNotFoundException {

        LendingEntity lending = lendingRepository.findActiveLendingByDeviceId(deviceId);
        if (lending == null) {
            throw new EntityNotFoundException(String.format(
                    "Couldn't apply. Because the device(ID: %s) is NOT lent.",
                    deviceId));
        }
        lending.setActualReturnDate(new Date());
    }
}
