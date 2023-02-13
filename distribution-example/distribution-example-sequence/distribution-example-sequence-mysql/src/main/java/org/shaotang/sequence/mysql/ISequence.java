package org.shaotang.sequence.mysql;

import java.util.List;

public interface ISequence<T extends Number> {

    /**
     * 获取序列下一个值
     * @param key 序列key
     * @return 下一个序列号
     */
    T nextVal(String key);

    /**
     * 异常获取count个key
     * @param key 序列key
     * @param count 获取count个sequence值
     * @return 下count个序列号
     */
    List<T> nextVal(String key, int count);

    T nextVal(String key, T current);

    List<T> nextVal(String var1, T current, int count);

    T currentVal(String key);
}