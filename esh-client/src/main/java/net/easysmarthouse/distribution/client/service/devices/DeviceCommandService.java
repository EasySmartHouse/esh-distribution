package net.easysmarthouse.distribution.client.service.devices;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import net.easysmarthouse.distribution.shared.DeviceCommand;
import net.easysmarthouse.distribution.shared.MapNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Service
public class DeviceCommandService implements MapNames {

    private HazelcastInstance hazelcastInstance;
    private IQueue<DeviceCommand> commandsQueue;

    @Autowired
    public DeviceCommandService(@Qualifier("ClientInstance")
                                        HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        commandsQueue = hazelcastInstance.getQueue(DEVICE_COMMAND_QUEUE);
    }

    public void addAll(Collection<DeviceCommand> commands) {
        commandsQueue.addAll(commands);
    }

    public DeviceCommand poll() {
        return commandsQueue.poll();
    }

}
