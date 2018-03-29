package net.easysmarthouse.distribution.client.service.devices;

import com.hazelcast.core.HazelcastInstance;
import net.easysmarthouse.distribution.client.HazelcastClientTestConfiguration;
import net.easysmarthouse.distribution.shared.CommandType;
import net.easysmarthouse.distribution.shared.DeviceCommand;
import net.easysmarthouse.distribution.storage.node.factory.StorageNodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HazelcastClientTestConfiguration.class})
public class DeviceCommandServiceTest {

    @Autowired
    private DeviceCommandService commandService;

    @Autowired
    @Qualifier("ClientInstance")
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private StorageNodeFactory storageNodeFactory;

    @Before
    public void setUp() throws Exception {
        hazelcastInstance.getQueue(DeviceService.DEVICE_COMMAND_QUEUE).clear();
    }

    @Test
    public void poll() throws Exception {
        System.out.println("***** poll *****");
        storageNodeFactory.ensureClusterSize(2);

        DeviceCommand comm1 = new DeviceCommand(UUID.randomUUID().toString(), 5l, CommandType.TurnOn, new byte[0]);
        DeviceCommand comm2 = new DeviceCommand(UUID.randomUUID().toString(), 5l, CommandType.TurnOff, new byte[0]);
        DeviceCommand comm3 = new DeviceCommand(UUID.randomUUID().toString(), 5l, CommandType.SetValue, new byte[0]);

        List<DeviceCommand> commands = new ArrayList<>();
        commands.add(comm1);
        commands.add(comm2);
        commands.add(comm3);

        commandService.addAll(commands);

        DeviceCommand commFromQueue1 = commandService.poll();
        assertEquals(comm1, commFromQueue1);

        DeviceCommand commFromQueue2 = commandService.poll();
        assertEquals(comm2, commFromQueue2);

        DeviceCommand commFromQueue3 = commandService.poll();
        assertEquals(comm3, commFromQueue3);
    }
}