package com.github.attemper.common.result.dispatch.tool;

import com.github.attemper.common.result.MapResult;

import java.util.List;

public class ServerInfoResult {

    protected List<MapResult<String, Object>> cpu;

    protected List<MapResult<String, Object>> memory;

    protected List<MapResult<String, Object>> machine;

    protected List<MapResult<String, Object>> jvm;

    protected List<MapResult<String, Object>> resource;

    protected List<MapResult<String, Object>> file;

    public List<MapResult<String, Object>> getCpu() {
        return cpu;
    }

    public ServerInfoResult setCpu(List<MapResult<String, Object>> cpu) {
        this.cpu = cpu;
        return this;
    }

    public List<MapResult<String, Object>> getMemory() {
        return memory;
    }

    public ServerInfoResult setMemory(List<MapResult<String, Object>> memory) {
        this.memory = memory;
        return this;
    }

    public List<MapResult<String, Object>> getMachine() {
        return machine;
    }

    public ServerInfoResult setMachine(List<MapResult<String, Object>> machine) {
        this.machine = machine;
        return this;
    }

    public List<MapResult<String, Object>> getJvm() {
        return jvm;
    }

    public ServerInfoResult setJvm(List<MapResult<String, Object>> jvm) {
        this.jvm = jvm;
        return this;
    }

    public List<MapResult<String, Object>> getResource() {
        return resource;
    }

    public ServerInfoResult setResource(List<MapResult<String, Object>> resource) {
        this.resource = resource;
        return this;
    }

    public List<MapResult<String, Object>> getFile() {
        return file;
    }

    public ServerInfoResult setFile(List<MapResult<String, Object>> file) {
        this.file = file;
        return this;
    }
}
