package net.easysmarthouse.distribution.storage.node.store;

import com.hazelcast.core.QueueStore;
import net.easysmarthouse.distribution.shared.HubEvent;
import net.easysmarthouse.distribution.storage.node.dao.HubEventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HubEventQueueStore implements QueueStore<HubEvent> {

    private HubEventDao hubEventDao;

    @Autowired
    public HubEventQueueStore(HubEventDao hubEventDao) {
        this.hubEventDao = hubEventDao;
    }

    @Override
    public void store(Long id, HubEvent hubEvent) {
        hubEventDao.save(hubEvent);
    }

    @Override
    public void storeAll(Map<Long, HubEvent> map) {
        List<HubEvent> entries = new ArrayList<>(map.size());

        map.entrySet().forEach(entry -> {
            entries.add(entry.getValue());
        });

        hubEventDao.saveAll(entries);
    }

    @Override
    public void delete(Long key) {
        hubEventDao.delete(
                hubEventDao.findById(key)
                        .get()
        );
    }

    @Override
    public void deleteAll(Collection<Long> collection) {
        hubEventDao.deleteAll(
                hubEventDao.findAll()
        );
    }

    @Override
    public HubEvent load(Long key) {
        return hubEventDao.findById(key)
                .get();
    }

    @Override
    public Map<Long, HubEvent> loadAll(Collection<Long> collection) {
        Iterable<HubEvent> entries = hubEventDao.findAllById(collection);
        Map<Long, HubEvent> map = new HashMap<>(collection.size());
        for (HubEvent entry : entries) {
            map.put(entry.getHubId(), entry);
        }
        return map;
    }

    @Override
    public Set<Long> loadAllKeys() {
        Iterable<HubEvent> entries = hubEventDao.findAll();
        Set<Long> keys = new HashSet<>();
        for (HubEvent entry : entries) {
            keys.add(entry.getHubId());
        }
        return keys;
    }
}
