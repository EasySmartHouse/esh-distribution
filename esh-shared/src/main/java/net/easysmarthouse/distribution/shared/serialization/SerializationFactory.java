package net.easysmarthouse.distribution.shared.serialization;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;
import net.easysmarthouse.distribution.shared.Device;

public class SerializationFactory implements PortableFactory {

    public static final int SERIALIZATION_FACTORY_ID = 1;

    @Override
    public Portable create(int classId) {
        switch (classId) {
            case Device.CLASS_ID:
                return new Device();
            default:
                return null;
        }
    }
}
