package net.easysmarthouse.distribution.storage.node.store;

import net.easysmarthouse.distribution.shared.Device;
import net.easysmarthouse.distribution.storage.node.dao.DeviceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DeviceMapStore implements com.hazelcast.core.MapStore<Long, Device> {

    private DeviceDao deviceDao;

    @Autowired
    public DeviceMapStore(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    @Override
    public void store(Long key, Device device) {
        deviceDao.save(device);
    }

    @Override
    public void storeAll(Map<Long, Device> map) {
        deviceDao.saveAll(map.values());
    }

    @Override
    public void delete(Long key) {
        Device device = load(key);
        deviceDao.delete(device);
    }

    @Override
    public void deleteAll(Collection<Long> keys) {
        Iterable<Device> devices = deviceDao.findAllById(keys);
        deviceDao.deleteAll(devices);
    }

    @Override
    public Device load(Long key) {
        return deviceDao.findById(key)
                .orElse(null);
    }

    @Override
    public Map<Long, Device> loadAll(Collection<Long> keys) {
        Iterable<Device> devices = deviceDao.findAllById(keys);
        return StreamSupport.stream(devices.spliterator(), false)
                .collect(Collectors.toMap(Device::getId, Function.identity()));
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        Iterable<Device> devices = deviceDao.findAll();
        return StreamSupport.stream(devices.spliterator(), false)
                .map(Device::getId)
                .collect(Collectors.toList());
    }
}
