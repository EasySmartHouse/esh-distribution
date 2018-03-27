package net.easysmarthouse.distribution.client.service.devices;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import net.easysmarthouse.distribution.client.HazelcastClientTestConfiguration;
import net.easysmarthouse.distribution.client.helper.DeviceGenerator;
import net.easysmarthouse.distribution.shared.Device;
import net.easysmarthouse.distribution.shared.DeviceType;
import net.easysmarthouse.distribution.storage.node.factory.StorageNodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HazelcastClientTestConfiguration.class})
public class DeviceServiceIntegrationTest {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    @Qualifier("ClientInstance")
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private StorageNodeFactory storageNodeFactory;

    @Before
    public void setUp() throws Exception {
        hazelcastInstance.getMap(DeviceService.DEVICES_MAP).clear();
    }

    //@Test
    public void testAddDevice() {
        System.out.println("***** addDevice *****");
        Device device = new Device(2134l, "Simple switch", "DF4534563456FF", DeviceType.Switch, "Device description");
        deviceService.addDevice(device);

        IMap<Long, Device> devicesMap = hazelcastInstance.getMap(DeviceService.DEVICES_MAP);
        assertEquals(1, devicesMap.size());
        assertEquals(device, devicesMap.get(2134l));
    }

    //@Test
    public void testAddDevices() {
        System.out.println("***** addDevices *****");
        Device device1 = new Device(2134l, "Simple switch 1", "DF4534563456FF", DeviceType.Switch, "Device description 1");
        Device device2 = new Device(2135l, "Simple switch 2", "DF4534563457FF", DeviceType.Switch, "Device description 2");
        Device device3 = new Device(2136l, "Simple switch 3", "DF4534563458FF", DeviceType.Switch, "Device description 3");

        List<Device> devices = Arrays.asList(device1, device2, device3);

        deviceService.addDevices(devices);

        IMap<Long, Device> deviceMap = hazelcastInstance.getMap(DeviceService.DEVICES_MAP);
        assertEquals(3, deviceMap.size());
        assertEquals(device1, deviceMap.get(2134l));
        assertEquals(device2, deviceMap.get(2135l));
        assertEquals(device3, deviceMap.get(2136l));
    }

    //@Test
    public void testNoDataLossWithOnlyOneNode() throws Exception {
        System.out.println("***** noDataLossWithOnlyOneNode *****");
        storageNodeFactory.ensureClusterSize(4);

        int maxDevices = 1000;
        List<Device> devices = new DeviceGenerator().generate(maxDevices);
        deviceService.addDevices(devices);

        Map<Long, Device> deviceMap = hazelcastInstance.getMap(DeviceService.DEVICES_MAP);
        assertEquals(maxDevices, deviceMap.size());

        storageNodeFactory.ensureClusterSize(1);
        assertEquals(maxDevices, deviceMap.size());
    }


    //@Test
    public void testNoDataLossAfterClusterShutdown() throws Exception {
        System.out.println("***** noDataLossAfterClusterShutdown *****");
        storageNodeFactory.ensureClusterSize(4);

        int maxDevices = 1000;
        List<Device> devices = new DeviceGenerator().generate(maxDevices);
        deviceService.addDevices(devices);

        Map<Long, Device> deviceMap = hazelcastInstance.getMap(DeviceService.DEVICES_MAP);
        assertEquals(maxDevices, deviceMap.size());

        storageNodeFactory.ensureClusterSize(0);
        storageNodeFactory.ensureClusterSize(3);

        assertEquals(maxDevices, deviceMap.size());
    }

    //@Test
    public void testFindDevicesByAddress() throws Exception {
        System.out.println("***** findDevicesByAddress *****");
        deviceService.addDevices(new DeviceGenerator().generate(1000));

        Collection<Device> devices = deviceService.findDeviceByAddress("DF4563456335455555");
        assertEquals(1, devices.size());
        assertEquals("Device 5", devices.iterator().next().getLabel());
    }

    @Test
    public void testUpdateDeviceState() throws Exception {
        System.out.println("***** updateDeviceState *****");
        deviceService.addDevices(new DeviceGenerator().generate(2));

        assertEquals("Device 1", deviceService.getDevice(1l).getLabel());

        boolean result = deviceService.updateDeviceStateWithLock(1l, "1323.34");
        assertTrue(result);

        assertEquals("1323.34", deviceService.getDevice(1l).getState());
    }

}