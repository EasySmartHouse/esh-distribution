package net.easysmarthouse.distribution.storage.node.dao;

import net.easysmarthouse.distribution.shared.HubEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubEventDao extends CrudRepository<HubEvent, Long> {
}
