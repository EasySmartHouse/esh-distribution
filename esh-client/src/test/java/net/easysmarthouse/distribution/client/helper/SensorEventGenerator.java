package net.easysmarthouse.distribution.client.helper;

import net.easysmarthouse.distribution.shared.EventType;
import net.easysmarthouse.distribution.shared.SensorEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SensorEventGenerator {

    private final long deviceId;
    private final double minValue;
    private final double maxValue;

    public SensorEventGenerator(long deviceId, double minValue, double maxValue) {
        this.deviceId = deviceId;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public List<SensorEvent> generate(int count) {
        List<SensorEvent> events = new ArrayList<>(count);
        Calendar current = Calendar.getInstance();
        for (long x = count; x > 0; x--) {
            current.add(Calendar.DATE, -1);
            double value = ((Math.abs(minValue - maxValue) <= 0.000001)) ? minValue : ThreadLocalRandom.current().nextDouble(minValue, maxValue);
            SensorEvent event = new SensorEvent(x, deviceId, EventType.ValueChanged, current.getTime(), value);
            events.add(event);
        }
        return events;
    }
}
