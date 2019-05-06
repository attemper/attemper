package com.github.attemper.config.base.id;

import org.camunda.bpm.engine.impl.cfg.IdGenerator;

public class SnowFlakeIdGenerator implements IdGenerator {

    private final static long START_TIMESTAMP = 946656000000L;  //2000-01-01 00:00:00

    private final static long SEQUENCE_BIT = 12;
    private final static long MACHINE_BIT = 5;
    private final static long DATA_CENTER_BIT = 5;

    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    private long dataCenterId;
    private long machineId;
    private long sequence = 0L;
    private long lastTimeStamp = -1L;

    public SnowFlakeIdGenerator(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    @Override
    public synchronized String getNextId() {
        long currTimeStamp = now();
        if (currTimeStamp < lastTimeStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currTimeStamp == lastTimeStamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currTimeStamp = getNextMill();
            }
        } else {
            sequence = 0L;
        }

        lastTimeStamp = currTimeStamp;

        return String.valueOf((currTimeStamp - START_TIMESTAMP) << TIMESTAMP_LEFT
                | dataCenterId << DATA_CENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence);
    }

    private long getNextMill() {
        long mill = now();
        while (mill <= lastTimeStamp) {
            mill = now();
        }
        return mill;
    }

    private long now() {
        return System.currentTimeMillis();
    }
}
