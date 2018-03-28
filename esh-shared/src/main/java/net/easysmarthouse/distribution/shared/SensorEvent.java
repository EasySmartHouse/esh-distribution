package net.easysmarthouse.distribution.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class SensorEvent implements Serializable {

    private Long eventId;
    private Long deviceId;
    private EventType eventType;
    private Date eventDateTime;
    private Double sensorValue;

    public SensorEvent() {
    }

    public SensorEvent(Long eventId, Long deviceId, EventType eventType, Date eventDateTime, Double sensorValue) {
        this.eventId = eventId;
        this.deviceId = deviceId;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
        this.sensorValue = sensorValue;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorEvent that = (SensorEvent) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(deviceId, that.deviceId) &&
                eventType == that.eventType &&
                Objects.equals(eventDateTime, that.eventDateTime) &&
                Objects.equals(sensorValue, that.sensorValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, deviceId, eventType, eventDateTime, sensorValue);
    }

    @Override
    public String toString() {
        return "DeviceEvent{" +
                "eventId=" + eventId +
                ", deviceId=" + deviceId +
                ", eventType=" + eventType +
                ", eventDateTime=" + eventDateTime +
                ", sensorValue=" + sensorValue +
                '}';
    }
}
