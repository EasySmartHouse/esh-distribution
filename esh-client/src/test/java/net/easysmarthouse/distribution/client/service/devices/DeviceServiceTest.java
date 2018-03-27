package net.easysmarthouse.distribution.client.service.devices;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import net.easysmarthouse.distribution.client.HazelcastClientTestConfiguration;
import net.easysmarthouse.distribution.shared.Device;
import net.easysmarthouse.distribution.shared.DeviceType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HazelcastClientTestConfiguration.class})
public class DeviceServiceTest {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    @Qualifier("ClientInstance")
    HazelcastInstance hazelcastInstance;

    @Before
    public void setUp() throws Exception {
        hazelcastInstance.getMap(DeviceService.DEVICES_MAP).clear();
    }

    @Test
    public void addDevice() {
        System.out.println("***** addDevice *****");
        Device device = new Device(2134l, "Simple switch", "DF4534563456FF", DeviceType.Switch, "Device description");
        deviceService.addDevice(device);

        IMap<Long, Device> devicesMap = hazelcastInstance.getMap(DeviceService.DEVICES_MAP);
        assertEquals(1, devicesMap.size());
        assertEquals(device, devicesMap.get(2134l));
    }
}