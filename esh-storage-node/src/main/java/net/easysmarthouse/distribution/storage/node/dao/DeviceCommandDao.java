package net.easysmarthouse.distribution.storage.node.dao;

import net.easysmarthouse.distribution.shared.DeviceCommandEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceCommandDao extends CrudRepository<DeviceCommandEntry, Long> {
}
