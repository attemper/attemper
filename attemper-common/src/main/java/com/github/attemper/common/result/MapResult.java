package com.github.attemper.common.result;

public class MapResult<K, V> {
    protected K name;

    protected V value;

    public MapResult() {

    }

    public MapResult(K name, V value) {
        this.name = name;
        this.value = value;
    }

    public K getName() {
        return name;
    }

    public MapResult<K, V> setName(K name) {
        this.name = name;
        return this;
    }

    public V getValue() {
        return value;
    }

    public MapResult<K, V> setValue(V value) {
        this.value = value;
        return this;
    }
}
