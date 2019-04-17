package com.sse.attemper.config.uuid.impl;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.sse.attemper.config.uuid.IdGenerator;

public class StrongUuidGenerator implements IdGenerator {

    // different ProcessEngines on the same classloader share one generator.
    protected static TimeBasedGenerator timeBasedGenerator;

    public StrongUuidGenerator() {
        ensureGeneratorInitialized();
    }

    protected void ensureGeneratorInitialized() {
        if (timeBasedGenerator == null) {
            synchronized (StrongUuidGenerator.class) {
                if (timeBasedGenerator == null) {
                    timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
                }
            }
        }
    }

    public String getNextId() {
        return timeBasedGenerator.generate().toString();
    }
}
