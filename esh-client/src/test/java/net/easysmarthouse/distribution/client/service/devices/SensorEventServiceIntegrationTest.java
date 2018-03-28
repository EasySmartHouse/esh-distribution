package net.easysmarthouse.distribution.client.service.devices;

import com.hazelcast.core.HazelcastInstance;
import net.easysmarthouse.distribution.client.HazelcastClientTestConfiguration;
import net.easysmarthouse.distribution.client.helper.SensorEventGenerator;
import net.easysmarthouse.distribution.client.service.event.SensorEventService;
import net.easysmarthouse.distribution.shared.SensorEvent;
import net.easysmarthouse.distribution.storage.node.factory.StorageNodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HazelcastClientTestConfiguration.class})
public class SensorEventServiceIntegrationTest {

    @Autowired
    private SensorEventService sensorEventService;

    @Autowired
    @Qualifier("ClientInstance")
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private StorageNodeFactory storageNodeFactory;

    @Before
    public void setUp() throws Exception {
        hazelcastInstance.getMap(DeviceService.SENSOR_EVENTS_MAP).clear();
    }

    @Test
    public void testSensorAverage() {
        sensorEventService.addEvents(
                new SensorEventGenerator(4l, 5.0, 5.0)
                        .generate(100)
        );

        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DATE, -5);
        Date start = cal.getTime();

        Double avg = sensorEventService.getAverageSensorValue(4l, start, end);
        assertEquals(5.0, avg, 0.001);
    }

}
