package net.easysmarthouse.distribution.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
public class Device implements Serializable {

    private static final long serialVersionUID = 4680916086924146017L;

    private Long id;
    private String label;
    private String address;
    private DeviceType deviceType;
    private String description;
    private boolean enable = true;
    private String state;

    public Device() {
    }

    public Device(Long id) {
        this.id = id;
    }

    public Device(Long id, String label, String address, DeviceType deviceType, String description) {
        this.id = id;
        this.label = label;
        this.address = address;
        this.deviceType = deviceType;
        this.description = description;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id) &&
                Objects.equals(label, device.label) &&
                Objects.equals(address, device.address) &&
                deviceType == device.deviceType &&
                Objects.equals(description, device.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, address, deviceType, description);
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", address='" + address + '\'' +
                ", deviceType=" + deviceType +
                ", description='" + description + '\'' +
                ", enable=" + enable +
                ", state=" + state +
                '}';
    }
}
