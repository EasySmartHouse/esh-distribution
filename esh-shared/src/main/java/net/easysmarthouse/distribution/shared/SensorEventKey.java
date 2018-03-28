package net.easysmarthouse.distribution.shared;

import com.hazelcast.core.PartitionAware;

import java.io.Serializable;

/**
 * To keep related data for performance
 */
public class SensorEventKey implements Serializable, PartitionAware<Long> {

    private Long deviceId;
    private Long eventId;

    public SensorEventKey() {
    }

    public SensorEventKey(Long deviceId, Long eventId) {
        this.deviceId = deviceId;
        this.eventId = eventId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public Long getPartitionKey() {
        return deviceId;
    }
}
