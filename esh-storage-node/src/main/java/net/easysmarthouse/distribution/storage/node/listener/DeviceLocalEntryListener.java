package net.easysmarthouse.distribution.storage.node.listener;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import net.easysmarthouse.distribution.shared.*;
import net.easysmarthouse.distribution.storage.node.factory.event.SensorEventFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Global event listener - listener on every client on whole cluster, then event receives every client (addEntryListener and use ClientInstance)
 * (Single cluster listener - For one receiver need to block thread on getMap("deviceMap") until EntryListener is removed)
 *
 * This implementation is local (for every cluster). Need for StorageNodeInstance. Much more scalable solution
 */
@Service
public class DeviceLocalEntryListener implements EntryAddedListener<Long, Device>, EntryUpdatedListener<Long, Device>, EntryRemovedListener<Long, Device> {

    @Autowired
    private SensorEventFactory eventFactory;

    private HazelcastInstance hazelcastInstance;

    @Autowired
    public DeviceLocalEntryListener(@Qualifier("StorageNodeInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        IMap<Long, Device> deviceMap = hazelcastInstance.getMap(MapNames.DEVICES_MAP);
        deviceMap.addLocalEntryListener(this);
    }

    @Override
    public void entryAdded(EntryEvent<Long, Device> entryEvent) {
        Device newDevice = entryEvent.getValue();
        if (newDevice.getDeviceType() == DeviceType.Sensor) {
            SensorEvent sensorEvent = eventFactory.createEvent(newDevice.getId(), EventType.DeviceConnected);

            hazelcastInstance.getMap(MapNames.SENSOR_EVENTS_MAP)
                    .put(sensorEvent.getEventId(), sensorEvent);
        }
    }

    @Override
    public void entryRemoved(EntryEvent<Long, Device> entryEvent) {
        Device removedDevice = entryEvent.getValue();
        if (removedDevice.getDeviceType() == DeviceType.Sensor) {
            SensorEvent sensorEvent = eventFactory.createEvent(removedDevice.getId(), EventType.DeviceConnected);

            hazelcastInstance.getMap(MapNames.SENSOR_EVENTS_MAP)
                    .put(sensorEvent.getEventId(), sensorEvent);
        }
    }

    @Override
    public void entryUpdated(EntryEvent<Long, Device> entryEvent) {
        Device updatedDevice = entryEvent.getValue();
        if (updatedDevice.getDeviceType() == DeviceType.Sensor) {
            SensorEvent sensorEvent = eventFactory.createEvent(updatedDevice.getId(), EventType.DeviceUpdated);

            hazelcastInstance.getMap(MapNames.SENSOR_EVENTS_MAP)
                    .put(sensorEvent.getEventId(), sensorEvent);
        }
    }
}
