package chapter.devops.javastreams;

import java.util.Random;
import java.util.stream.Stream;

public class SimpleInfiniteStream {

    public static void main(String[] args) {
        new SimpleInfiniteStream().run();
    }

    private Random random = new Random();

    private void run() {
        Stream.generate(this::poll)
                .takeWhile(n -> n > 10000000L)
                .map(this::processAndTransform)
                .forEach(System.out::println)
        ;
    }

    private long poll() {
        long cnt = 0;
        while (random.nextLong() % 2000000 != 0) {
            cnt++;
        }
        return cnt;
    }

    private String processAndTransform(long value) {
        return "Awaiting: " + value;
    }

}
