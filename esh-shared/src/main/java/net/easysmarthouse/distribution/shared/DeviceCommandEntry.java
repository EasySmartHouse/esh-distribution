package net.easysmarthouse.distribution.shared;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class DeviceCommandEntry {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private DeviceCommand deviceCommand;

    public DeviceCommandEntry() {
    }

    public DeviceCommandEntry(Long id, DeviceCommand deviceCommand) {
        this.id = id;
        this.deviceCommand = deviceCommand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeviceCommand getDeviceCommand() {
        return deviceCommand;
    }

    public void setDeviceCommand(DeviceCommand deviceCommand) {
        this.deviceCommand = deviceCommand;
    }
}
