package net.easysmarthouse.distribution.shared.processor;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.query.SqlPredicate;
import net.easysmarthouse.distribution.shared.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Without BackupEntryProcessor because it will not update any data on the map
 */
public class GetSensorEventsOverviewEntryProcessor implements Serializable, EntryProcessor<Long, Device>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Object process(Map.Entry<Long, Device> entry) {
        Long deviceKey = entry.getKey();
        Device device = entry.getValue();

        IMap<SensorEventKey, SensorEvent> sensorEventMap = hazelcastInstance.getMap(MapNames.SENSOR_EVENTS_MAP);
        Set<SensorEventKey> eventKeys = sensorEventMap.localKeySet(new SqlPredicate("deviceId = " + deviceKey));
        List<SensorEvent> events = new ArrayList<>(eventKeys.size());
        for (SensorEventKey eventKey : eventKeys) {
            events.add(sensorEventMap.get(eventKey));
        }

        return new SensorEventsOverview(device.getLabel(), device.getAddress(), events);
    }

    @Override
    public EntryBackupProcessor<Long, Device> getBackupProcessor() {
        return null;
    }
}
