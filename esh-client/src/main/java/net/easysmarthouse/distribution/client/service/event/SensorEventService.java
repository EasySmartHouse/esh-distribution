package net.easysmarthouse.distribution.client.service.event;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.aggregation.Aggregation;
import com.hazelcast.mapreduce.aggregation.Aggregations;
import net.easysmarthouse.distribution.shared.SensorEvent;
import net.easysmarthouse.distribution.shared.supplier.SensorValueSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensorEventService {

    private HazelcastInstance hazelcastInstance;
    private IMap<Long, SensorEvent> eventsMap;

    @Autowired
    public SensorEventService(@Qualifier("ClientInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() throws Exception {
        eventsMap = hazelcastInstance.getMap("events");
    }

    public void addEvents(Collection<SensorEvent> events) {
        Map<Long, SensorEvent> eventsLocalMap = new HashMap<>();
        events.forEach(event ->
                eventsLocalMap.put(event.getEventId(), event)
        );
        eventsMap.putAll(eventsLocalMap);
    }

    public Double getAverageSensorValue(Long deviceId, Date start, Date end) {
        SensorValueSupplier supplier = new SensorValueSupplier(deviceId, start, end);
        Aggregation<Long, Double, Double> aggregation = Aggregations.doubleAvg();
        return eventsMap.aggregate(supplier, aggregation);
    }
}
