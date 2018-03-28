package net.easysmarthouse.distribution.shared;

import java.io.Serializable;
import java.util.List;

public class SensorEventsOverview implements Serializable {

    private String deviceLabel;
    private String deviceAddress;
    private List<SensorEvent> events;

    public SensorEventsOverview() {
    }

    public SensorEventsOverview(String deviceLabel, String deviceAddress, List<SensorEvent> events) {
        this.deviceLabel = deviceLabel;
        this.deviceAddress = deviceAddress;
        this.events = events;
    }

    public String getDeviceLabel() {
        return deviceLabel;
    }

    public void setDeviceLabel(String deviceLabel) {
        this.deviceLabel = deviceLabel;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public List<SensorEvent> getEvents() {
        return events;
    }

    public void setEvents(List<SensorEvent> events) {
        this.events = events;
    }
}
