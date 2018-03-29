package net.easysmarthouse.distribution.storage.node.factory.event;

import net.easysmarthouse.distribution.shared.EventType;
import net.easysmarthouse.distribution.shared.SensorEvent;

public interface SensorEventFactory {

    public SensorEvent createEvent(Long deviceId, EventType eventType);

}
