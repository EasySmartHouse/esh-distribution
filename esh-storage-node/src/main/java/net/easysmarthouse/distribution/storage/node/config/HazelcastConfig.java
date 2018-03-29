package net.easysmarthouse.distribution.storage.node.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import net.easysmarthouse.distribution.storage.node.store.DeviceCommandQueueStore;
import net.easysmarthouse.distribution.storage.node.store.DeviceMapStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static net.easysmarthouse.distribution.shared.MapNames.DEVICES_MAP;
import static net.easysmarthouse.distribution.shared.MapNames.DEVICE_COMMAND_QUEUE;

@Configuration
public class HazelcastConfig {

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance createStorageNode(@Qualifier("StorageNodeConfig") Config config)
            throws Exception {
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean(name = "StorageNodeConfig")
    public Config config(DeviceMapStore deviceMapStore, DeviceCommandQueueStore deviceCommandQueueStore) throws Exception {
        Config config = new Config();

        //Device command queue configuration
        QueueConfig commandQueueConfig = new QueueConfig();
        commandQueueConfig.setName(DEVICE_COMMAND_QUEUE);
        QueueStoreConfig queueStoreConfig = new QueueStoreConfig();
        queueStoreConfig.setStoreImplementation(deviceCommandQueueStore);
        
        commandQueueConfig.setQueueStoreConfig(queueStoreConfig);
        config.addQueueConfig(commandQueueConfig);

        //Create a new map configuration for the devices map
        MapConfig deviceMapConfig = new MapConfig();

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
