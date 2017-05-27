package jp.co.fujixerox.deviceman.service;

import jp.co.fujixerox.deviceman.persistence.entity.DeviceEntity;
import jp.co.fujixerox.deviceman.persistence.entity.type.OsName;
import jp.co.fujixerox.deviceman.persistence.repository.DeviceRepository;
import jp.co.fujixerox.deviceman.persistence.repository.LendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class DeviceService {
    private final DeviceRepository repository;
    private final LendingRepository lendingRepository;

    @Autowired
    public DeviceService(
            DeviceRepository repository,
            LendingRepository lendingRepository) {
        this.repository = repository;
        this.lendingRepository = lendingRepository;
    }

    /**
     * Search devices with query.
     *
     * @param query searching query. target are ID, IMEI, WiFi Mac Address or name.
     * @return
     */
    public List<DeviceEntity> search(String query) {
        List<DeviceEntity> devices = repository.search(query);
        devices.forEach((deviceEntity -> {
            deviceEntity.setActiveLending(lendingRepository.findActiveLendingByDeviceId(deviceEntity.getId()));
        }));
        return repository.search(query);
    }

    /**
     * Register new device entity.
     *
     * @param name           Name of the new device.
     * @param manufacturer   Manufacturer of the new device.
     * @param osNameString   OS name of the device.
     * @param osVersion      OS version of the deivce
     * @param imei           ID of the new device.
     * @param wifiMacAddress Address of the new device.
     * @param phoneNumber    Phone number of th device.
     * @return Persisted {@link DeviceEntity} instance.
     * @throws IllegalArgumentException thrown if provided OS name is not support type.
     * @throws ConflictException        thrown if provided imei or WiFi Mac Address is registered.
     */
    @Transactional(readOnly = false)
    public DeviceEntity create(
            String name,
            String manufacturer,
            String osNameString,
            String osVersion,
            String imei,
            String wifiMacAddress,
            String phoneNumber) throws IllegalArgumentException, ConflictException {

        OsName osName = OsName.forName(osNameString);

        if (osName == null) {
            throw new IllegalArgumentException("Provided OS name is NOT supporting.");
        }
        if (repository.existsByImei(imei) || repository.existsByWifiMacAddress(wifiMacAddress)) {
            throw new ConflictException("cannot register as a result of to conflict IMEI or WifiMac Address.");
        }

        DeviceEntity creating = new DeviceEntity(
                name,
                manufacturer,
                osName,
                osVersion,
                imei,
                wifiMacAddress);

        if (phoneNumber != null) {
            creating.setPhoneNumber(phoneNumber);
        }

        return repository.save(creating);
    }
}
