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

    @Test
    public void testTryAdvance() {

        while (spliterator.tryAdvance(num -> System.out.println(num + ":" + round.getAndIncrement()))) {
            System.out.println(loop.getAndIncrement());
        }
    }

    @Test
    public void testForEachRemaining() {

        spliterator.forEachRemaining(num -> System.out.println(num + ":" + round.getAndIncrement()));
    }


    public static void main(String[] args) {
    }
}
