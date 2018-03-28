package net.easysmarthouse.distribution.shared.supplier;

import com.hazelcast.mapreduce.aggregation.Supplier;
import net.easysmarthouse.distribution.shared.SensorEvent;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class SensorValueSupplier extends Supplier<Long, SensorEvent, Double>
        implements Serializable {

    private final Long sensorId;
    private final Date start;
    private final Date end;

    public SensorValueSupplier(Long sensorId, Date start, Date end) {
        this.sensorId = sensorId;
        this.start = start;
        this.end = end;
    }

    @Override
    public Double apply(Map.Entry<Long, SensorEvent> entry) {
        if (!sensorId.equals(entry.getValue().getDeviceId())) {
            return null;
        }

        Date eventDate = entry.getValue().getEventDateTime();
        if (eventDate.compareTo(start) >= 0 && eventDate.compareTo(end) < 0) {
            return entry.getValue().getSensorValue();
        } else {
            return null;
        }
    }
}
