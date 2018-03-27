package net.easysmarthouse.distribution.storage.node.dao;

import net.easysmarthouse.distribution.shared.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDao extends CrudRepository<Device, Long> {
}
