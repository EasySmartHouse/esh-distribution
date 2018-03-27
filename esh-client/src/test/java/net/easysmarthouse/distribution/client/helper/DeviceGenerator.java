package net.easysmarthouse.distribution.client.helper;

import net.easysmarthouse.distribution.shared.Device;
import net.easysmarthouse.distribution.shared.DeviceType;

import java.util.ArrayList;
import java.util.List;

public class DeviceGenerator {

    public List<Device> generate(int maxDevices) throws Exception {
        List<Device> devices = new ArrayList<>(maxDevices);

        for (long x = 0; x < maxDevices; x++) {
            devices.add(
                    new Device(x,
                            "Device " + x,
                            "DF456345633545555" + x,
                            DeviceType.Sensor,
                            "Sensor " + x
                    )
            );
        }

        return devices;
    }

}
