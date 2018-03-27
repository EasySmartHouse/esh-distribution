package net.easysmarthouse.distribution.storage.node.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import net.easysmarthouse.distribution.storage.node.store.DeviceMapStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static net.easysmarthouse.distribution.shared.MapNames.DEVICES_MAP;

@Configuration
public class HazelcastConfig {

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance createStorageNode(@Qualifier("StorageNodeConfig") Config config)
            throws Exception {
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean(name = "StorageNodeConfig")
    public Config config(DeviceMapStore deviceMapStore) throws Exception {
        Config config = new Config();

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
