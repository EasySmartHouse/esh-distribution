package net.easysmarthouse.distribution.client.service.devices;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import net.easysmarthouse.distribution.shared.Device;
import net.easysmarthouse.distribution.shared.MapNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class DeviceService implements MapNames {

    private HazelcastInstance hazelcastInstance;
    private IMap<Long, Device> devicesMap;

    @Autowired
    public DeviceService(@Qualifier("ClientInstance")
                                 HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        devicesMap = hazelcastInstance.getMap(DEVICES_MAP);
        devicesMap.addIndex("label", true);
    }

    public Device getDevice(Long key) {
        return devicesMap.get(key);
    }

    public void addDevice(Device device) {
        devicesMap.put(device.getId(), device);
    }

    public void addDevices(Collection<Device> devices) {
        Map<Long, Device> devicesLocalMap = new HashMap<>();
        devices.forEach(device ->
                devicesLocalMap.put(device.getId(), device)
        );
        devicesMap.putAll(devicesLocalMap);
    }

    public Collection<Device> findDeviceByAddress(String address) {
        SqlPredicate addressPredicate = new SqlPredicate("address = '" + address + "'");
        return devicesMap.values(addressPredicate);
    }

    public void updateDevice(Device device) {
        devicesMap.put(device.getId(), device);
    }

    public boolean updateDeviceWithLock(Long deviceId, Function<Device, Device> function) {
        try {
            boolean lockObtained = devicesMap.tryLock(deviceId, 2, TimeUnit.SECONDS);
            try {
                if (!lockObtained) {
                    return false;
                }
                Device oldDevice = getDevice(deviceId);
                Device newDevice = function.apply(oldDevice);
                devicesMap.put(deviceId, newDevice);
            } finally {
                devicesMap.unlock(deviceId);
            }
            return true;
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean updateDeviceStateWithLock(Long deviceId, String deviceState) {
        try {
            boolean lockObtained = devicesMap.tryLock(deviceId, 2, TimeUnit.SECONDS);
            try {
                if (!lockObtained) {
                    return false;
                }
                Device device = getDevice(deviceId);
                device.setState(deviceState);
                devicesMap.put(deviceId, device);
            } finally {
                devicesMap.unlock(deviceId);
            }
            return true;
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteDevice(Device device) {
        devicesMap.delete(device.getId());
    }

}
