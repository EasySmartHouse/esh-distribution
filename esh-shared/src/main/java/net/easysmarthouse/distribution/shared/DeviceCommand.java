package net.easysmarthouse.distribution.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class DeviceCommand implements Serializable {

    @Id
    private String uuid;
    private Long deviceId;
    private CommandType commandType;
    private byte[] commandValue;

    public DeviceCommand() {
    }

    public DeviceCommand(String uuid, Long deviceId, CommandType commandType, byte[] commandValue) {
        this.uuid = uuid;
        this.deviceId = deviceId;
        this.commandType = commandType;
        this.commandValue = commandValue;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public byte[] getCommandValue() {
        return commandValue;
    }

    public void setCommandValue(byte[] commandValue) {
        this.commandValue = commandValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceCommand that = (DeviceCommand) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(deviceId, that.deviceId) &&
                commandType == that.commandType &&
                Arrays.equals(commandValue, that.commandValue);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(uuid, deviceId, commandType);
        result = 31 * result + Arrays.hashCode(commandValue);
        return result;
    }
}
