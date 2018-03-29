package net.easysmarthouse.distribution.storage.node.factory.event.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IdGenerator;
import net.easysmarthouse.distribution.shared.EventType;
import net.easysmarthouse.distribution.shared.GeneratorNames;
import net.easysmarthouse.distribution.shared.SensorEvent;
import net.easysmarthouse.distribution.storage.node.factory.event.SensorEventFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class SensorEventFactoryImpl implements SensorEventFactory {

    private IdGenerator eventIdGen;
    private HazelcastInstance hazelcastInstance;

    @Autowired
    public SensorEventFactoryImpl(@Qualifier("StorageNodeInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        this.eventIdGen = hazelcastInstance.getIdGenerator(GeneratorNames.eventIdGenerator);
    }

    @Override
    public SensorEvent createEvent(Long deviceId, EventType eventType) {
        SensorEvent sensorEvent = new SensorEvent();
        sensorEvent.setDeviceId(deviceId);
        sensorEvent.setEventDateTime(new Date());
        sensorEvent.setEventId(eventIdGen.newId());
        sensorEvent.setEventType(eventType);
        return sensorEvent;
    }
}
