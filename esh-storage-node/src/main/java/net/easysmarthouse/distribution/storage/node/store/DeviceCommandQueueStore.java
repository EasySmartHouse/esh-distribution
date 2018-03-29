package net.easysmarthouse.distribution.storage.node.store;

import com.hazelcast.core.QueueStore;
import net.easysmarthouse.distribution.shared.DeviceCommand;
import net.easysmarthouse.distribution.shared.DeviceCommandEntry;
import net.easysmarthouse.distribution.storage.node.dao.DeviceCommandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceCommandQueueStore implements QueueStore<DeviceCommand> {

    private DeviceCommandDao deviceCommandDao;

    @Autowired
    public DeviceCommandQueueStore(DeviceCommandDao deviceCommandDao) {
        this.deviceCommandDao = deviceCommandDao;
    }

    @Override
    public void store(Long id, DeviceCommand deviceCommand) {
        deviceCommandDao.save(new DeviceCommandEntry(id, deviceCommand));
    }

    @Override
    public void storeAll(Map<Long, DeviceCommand> map) {
        List<DeviceCommandEntry> entries = new ArrayList<>(map.size());

        map.entrySet().forEach(entry -> {
            DeviceCommandEntry dcEntry = new DeviceCommandEntry(entry.getKey(), entry.getValue());
            entries.add(dcEntry);
        });

        deviceCommandDao.saveAll(entries);
    }

    @Override
    public void delete(Long key) {
        deviceCommandDao.delete(
                deviceCommandDao.findById(key)
                        .get()
        );
    }

    @Override
    public void deleteAll(Collection<Long> collection) {
        deviceCommandDao.deleteAll(
                deviceCommandDao.findAll()
        );
    }

    @Override
    public DeviceCommand load(Long key) {
        return deviceCommandDao.findById(key)
                .get()
                .getDeviceCommand();
    }

    @Override
    public Map<Long, DeviceCommand> loadAll(Collection<Long> collection) {
        Iterable<DeviceCommandEntry> entries = deviceCommandDao.findAllById(collection);
        Map<Long, DeviceCommand> map = new HashMap<>(collection.size());
        for (DeviceCommandEntry entry : entries) {
            map.put(entry.getId(), entry.getDeviceCommand());
        }
        return map;
    }

    @Override
    public Set<Long> loadAllKeys() {
        Iterable<DeviceCommandEntry> entries = deviceCommandDao.findAll();
        Set<Long> keys = new HashSet<>();
        for (DeviceCommandEntry entry : entries) {
            keys.add(entry.getId());
        }
        return keys;
    }
}
