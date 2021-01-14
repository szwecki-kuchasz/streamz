package chapter.devops.javastreams;

import org.junit.Test;

import java.util.Random;
import java.util.stream.Stream;

public class SimpleInfiniteStreamz {

    private Random random = new Random();

    @Test
    public void infiniteRandom() {
        random.ints()
                .skip(5)
                .limit(100)
                .forEach(System.out::println);
    }

    @Test
    public void ourInfinite() {
        Stream.generate(this::poll)
                // .limit(10)
                .map(this::processAndTransform)
                .forEach(System.out::println)
        ;
    }

    private long poll() {
        long cnt = 0;
        while (random.nextLong() % 10000000 != 0) {
            cnt++;
        }
        return cnt;
    }

    private String processAndTransform(long value) {
        return "Trafienie po " + value + " próbach";
    }

}
