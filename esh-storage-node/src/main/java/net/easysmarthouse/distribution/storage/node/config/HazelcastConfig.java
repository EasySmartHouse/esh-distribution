package net.easysmarthouse.distribution.storage.node.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import net.easysmarthouse.distribution.storage.node.listener.HubListener;
import net.easysmarthouse.distribution.storage.node.store.DeviceCommandQueueStore;
import net.easysmarthouse.distribution.storage.node.store.DeviceMapStore;
import net.easysmarthouse.distribution.storage.node.store.HubEventQueueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static net.easysmarthouse.distribution.shared.MapNames.DEVICES_MAP;
import static net.easysmarthouse.distribution.shared.MapNames.DEVICE_COMMAND_QUEUE;
import static net.easysmarthouse.distribution.shared.MapNames.HUB_EVENTS_QUEUE;

@Configuration
public class HazelcastConfig {

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance createStorageNode(@Qualifier("StorageNodeConfig") Config config)
            throws Exception {
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean(name = "StorageNodeConfig")
    public Config config(DeviceMapStore deviceMapStore, DeviceCommandQueueStore deviceCommandQueueStore,
                         HubEventQueueStore hubEventQueueStore, HubListener hubListener) throws Exception {
        Config config = new Config();

        //Hub listener
        ListenerConfig listenerConfig = new ListenerConfig();
        listenerConfig.setImplementation(hubListener);
        config.addListenerConfig(listenerConfig);

        //Device command queue configuration
        QueueConfig commandQueueConfig = new QueueConfig();
        commandQueueConfig.setName(DEVICE_COMMAND_QUEUE);
        QueueStoreConfig queueStoreConfig = new QueueStoreConfig();
        queueStoreConfig.setStoreImplementation(deviceCommandQueueStore);

        commandQueueConfig.setQueueStoreConfig(queueStoreConfig);
        config.addQueueConfig(commandQueueConfig);

        //Hub events
        QueueConfig hubQueueConfig = new QueueConfig();
        commandQueueConfig.setName(HUB_EVENTS_QUEUE);
        QueueStoreConfig hubStoreConfig = new QueueStoreConfig();
        queueStoreConfig.setStoreImplementation(hubEventQueueStore);

        hubQueueConfig.setQueueStoreConfig(hubStoreConfig);
        config.addQueueConfig(hubQueueConfig);

        //Create a new map configuration for the devices map
        MapConfig deviceMapConfig = new MapConfig();
        //increase the number of synchronous backup of the data
        //default is '1'
        deviceMapConfig.setBackupCount(3);
        //Or increase the number of async backups of the data
        //deviceMapConfig.setAsyncBackupCount(3);

        //Create a map store config for the device information
        MapStoreConfig deviceMapStoreConfig = new MapStoreConfig();
        deviceMapStoreConfig.setImplementation(deviceMapStore);
        //Enable async storing, by default - 0, that means to write immediately
        //this much performance
        deviceMapStoreConfig.setWriteDelaySeconds(3);

        //Update the device map configuration to use the
        //device map store config we just created
        deviceMapConfig.setMapStoreConfig(deviceMapStoreConfig);
        deviceMapConfig.setName(DEVICES_MAP);

        MapIndexConfig addressFieldIndex = new MapIndexConfig("address", true);
        deviceMapConfig.addMapIndexConfig(addressFieldIndex);

        //Add the devices map config to our storage node config
        config.addMapConfig(deviceMapConfig);

        return config;
    }

}
