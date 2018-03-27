package net.easysmarthouse.distribution.client.service.devices;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import net.easysmarthouse.distribution.shared.Device;
import net.easysmarthouse.distribution.shared.MapNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

}
