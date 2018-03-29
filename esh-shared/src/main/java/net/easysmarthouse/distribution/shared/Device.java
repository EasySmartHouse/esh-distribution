package net.easysmarthouse.distribution.shared;

import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.nio.serialization.VersionedPortable;
import net.easysmarthouse.distribution.shared.serialization.SerializationFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.Objects;

@Entity
public class Device implements VersionedPortable {

    public static final int CLASS_ID = 1;
    public static final int VERSION_ID = 1;

    public static final String ID_FIELD = "id";
    public static final String LABEL_FIELD = "label";
    public static final String ADDRESS_FIELD = "address";
    public static final String DEVICE_TYPE_FIELD = "deviceType";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String ENABLE_FIELD = "enable";
    public static final String STATE_FIELD = "state";

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

    @Transient
    @Override
    public int getFactoryId() {
        return SerializationFactory.SERIALIZATION_FACTORY_ID;
    }

    @Transient
    @Override
    public int getClassId() {
        return CLASS_ID;
    }

    @Transient
    @Override
    public int getClassVersion() {
        return VERSION_ID;
    }

    @Override
    public void writePortable(PortableWriter writer) throws IOException {
        writer.writeLong(ID_FIELD, id);
        writer.writeUTF(LABEL_FIELD, label);
        writer.writeUTF(ADDRESS_FIELD, address);
        writer.writeUTF(DEVICE_TYPE_FIELD, deviceType.name());
        if (description != null) {
            writer.writeUTF(DESCRIPTION_FIELD, description);
        }
        writer.writeBoolean(ENABLE_FIELD, enable);
        if (state != null) {
            writer.writeUTF(STATE_FIELD, state);
        }
    }

    @Override
    public void readPortable(PortableReader reader) throws IOException {
        if (reader.hasField(ID_FIELD)) {
            id = reader.readLong(ID_FIELD);
        }
        if (reader.hasField(LABEL_FIELD)) {
            label = reader.readUTF(LABEL_FIELD);
        }
        if (reader.hasField(ADDRESS_FIELD)) {
            address = reader.readUTF(ADDRESS_FIELD);
        }
        if (reader.hasField(DEVICE_TYPE_FIELD)) {
            deviceType = DeviceType.valueOf(reader.readUTF(DEVICE_TYPE_FIELD));
        }
        if (reader.hasField(DESCRIPTION_FIELD)) {
            description = reader.readUTF(DESCRIPTION_FIELD);
        }
        if (reader.hasField(ENABLE_FIELD)) {
            enable = reader.readBoolean(ENABLE_FIELD);
        }
        if (reader.hasField(STATE_FIELD)) {
            state = reader.readUTF(STATE_FIELD);
        }
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
