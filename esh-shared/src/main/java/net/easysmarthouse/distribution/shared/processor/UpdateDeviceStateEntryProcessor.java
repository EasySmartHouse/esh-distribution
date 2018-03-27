package net.easysmarthouse.distribution.shared.processor;

import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import net.easysmarthouse.distribution.shared.Device;

import java.io.Serializable;
import java.util.Map;

/**
 * Adventages:
 * - data is updated directly on the storage node
 * - reduced serialized object size
 * - avoid synchronization points in client code
 * - thread safe
 * - reduced complexity
 */
public class UpdateDeviceStateEntryProcessor implements Serializable,
        EntryProcessor<Long, Device>, EntryBackupProcessor<Long, Device> {

    private String newState;

    public UpdateDeviceStateEntryProcessor(String newState) {
        this.newState = newState;
    }

    @Override
    public void processBackup(Map.Entry<Long, Device> entry) {
        process(entry);
    }

    @Override
    public Object process(Map.Entry<Long, Device> entry) {
        Device device = entry.getValue();
        device.setState(newState);
        entry.setValue(device);
        return true;
    }

    @Override
    public EntryBackupProcessor<Long, Device> getBackupProcessor() {
        return this;
    }
}
