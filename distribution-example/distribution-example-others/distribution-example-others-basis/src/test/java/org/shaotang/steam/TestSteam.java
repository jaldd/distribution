package org.shaotang.steam;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;

public class TestSteam {

    List<Integer> list = new ArrayList<>();
    Spliterator<Integer> spliterator = list.stream().spliterator();
    AtomicInteger round = new AtomicInteger(1);
    AtomicInteger loop = new AtomicInteger(1);

    @Before
    public void before() {
        list.add(2);
        list.add(1);
        list.add(3);
    }

    //如果Spliterator中存在剩余元素，则对其中的某个元素执行传入的action回调，并且返回true，否则返回false。如果Spliterator启用了ORDERED特性，
    // 会按照顺序（这里的顺序值可以类比为ArrayList中容器数组元素的下标，ArrayList中添加新元素是天然有序的，下标由零开始递增）处理下一个元素
    @Test
    public void testTryAdvance() {

        while (spliterator.tryAdvance(num -> System.out.println(num + ":" + round.getAndIncrement()))) {
            System.out.println(loop.getAndIncrement());
        }
    }

    //如果Spliterator中存在剩余元素，则对其中的「所有剩余元素」在「当前线程中」执行传入的action回调。如果Spliterator启用了ORDERED特性，会按照顺序处理剩余所有元素。
    // 这是一个接口默认方法，方法体比较粗暴，直接是一个死循环包裹着tryAdvance()方法，直到false退出循环
    @Test
    public void testForEachRemaining() {

        spliterator.forEachRemaining(num -> System.out.println(num + ":" + round.getAndIncrement()));
    }


    //如果当前的Spliterator是可分区（可分割）的，那么此方法将会返回一个全新的Spliterator实例，这个全新的Spliterator实例里面的元素不会被当前Spliterator
    // 实例中的元素覆盖（这里是直译了API注释，实际要表达的意思是：当前的Spliterator实例X是可分割的，trySplit()方法会分割X产生一个全新的Spliterator实例Y，
    // 原来的X所包含的元素（范围）也会收缩，类似于X = [a,b,c,d] => X = [a,b], Y = [c,d]；如果当前的Spliterator实例X是不可分割的，此方法会返回NULL），「具体的分割算法由实现类决定」
    @Test
    public void testTrySplit() {

        Spliterator<Integer> first = spliterator;
        Spliterator<Integer> second = first.trySplit();
        first.forEachRemaining(System.out::println);
        second.forEachRemaining(System.out::println);
    }

    public static void main(String[] args) {
    }
}
