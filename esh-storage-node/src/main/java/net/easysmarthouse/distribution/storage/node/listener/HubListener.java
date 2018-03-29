package net.easysmarthouse.distribution.storage.node.listener;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.partition.PartitionLostEvent;
import com.hazelcast.partition.PartitionLostListener;
import net.easysmarthouse.distribution.shared.HubEvent;
import net.easysmarthouse.distribution.shared.HubEventType;
import net.easysmarthouse.distribution.shared.MapNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class HubListener implements PartitionLostListener {

    private HazelcastInstance hazelcastInstance;

    @Autowired
    public HubListener(@Qualifier("StorageNodeInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void partitionLost(PartitionLostEvent partitionLostEvent) {
        HubEvent hubEvent = new HubEvent();
        hubEvent.setHubId((long) partitionLostEvent.getPartitionId());
        hubEvent.setHubEventType(HubEventType.Lost);

        hazelcastInstance.getMap(MapNames.HUB_EVENTS_QUEUE)
                .put(hubEvent.getHubId(), hubEvent);
    }
}
