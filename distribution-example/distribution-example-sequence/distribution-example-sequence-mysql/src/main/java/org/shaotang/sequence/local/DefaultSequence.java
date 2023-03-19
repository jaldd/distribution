package org.shaotang.sequence.local;

import org.shaotang.sequence.ISequence;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSequence<T extends Number> implements ISequence<T> {

    private Map<String, T> sequenceMap = new ConcurrentHashMap<>();

    @Override
    public T nextVal(String key) {
        return null;
    }

    @Override
    public List<T> nextVal(String key, int count) {
        return null;
    }

    @Override
    public T nextVal(String key, T current) {
        return nextVal(key, current, 0).get(0);
    }

    @Override
    public List<T> nextVal(String key, T current, int count) {
        return null;
    }

    @Override
    public T currentVal(String key) {
        return sequenceMap.get(key);
    }
}
