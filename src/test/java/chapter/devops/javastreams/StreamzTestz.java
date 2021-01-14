package chapter.devops.javastreams;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class StreamzTestz {

    @Test
    public void createFiniteStreamOfIntegerAndPrintIt() {
        Stream.of(123, -5, 87324, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, -3287, 816, 34897, 1265)
                .forEach(
                        everyElementOfStreamPassedToForEachMethod
                                -> System.out.print(" " + everyElementOfStreamPassedToForEachMethod
                        ));
    }

    @Test
    public void asAboveButStreamCreatedFromArray1() {
        Integer[] anArray = {123, -5, 87324, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, -3287, 816, 34897, 1265};

        Stream<Integer> stream = Arrays.stream(anArray);
        stream
                .forEach(
                        justx
                                -> System.out.print(" " + justx
                        ));
    }

    @Test
    public void asAboveButStreamCreatedFromArray2() {
        Integer[] anArray = {123, -5, 87324, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, -3287, 816, 34897, 1265};
        Stream.of(anArray)
                .forEach(
                        justX
                                -> System.out.print(" " + justX
                        ));
    }

    @Test
    public void andFromList() {
        List<String> list = new ArrayList<>();
        list.add("Papa");
        list.add("Quebec");
        list.add("Romeo");
        list.add("Sierra");

        list.stream()
                .forEach(
                        justX
                                -> System.out.print(" " + justX
                        ));
    }

    @Test
    public void simpleMapStringToString() {
        createLoremIpsumStream()
                .map(originalText -> originalText.toUpperCase())
                .forEach(System.out::println);
    }

    @Test
    public void simpleMapStringToItsLength() {
        int maxLength = createLoremIpsumStream()
                .map(originalText -> originalText.length())
                .max(Integer::compare).get();
        System.out.println("Max length is: " + maxLength);
    }

    @Test
    public void manyNonterminalOpsOnlyOneTerminalOp() {
        Stream.of(123, -5, 87324, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, -3287, 816, 34897, 1265)
                // non-terminal
                .sorted()
                .sorted((x, y) -> y-x)
                .filter(z -> z > 0)
                .distinct()
                // terminal
                .forEach(bruce -> System.out.println("... " + bruce)); // lambda
    }

    @Test
    public void asAboveButWithMethodReference() {
        Stream.of(123, -5, 87324, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, -3287, 816, 34897, 1265)
                // non-terminal
                .sorted()
                .sorted((x, y) -> y-x)
                .filter(z -> z > 0)
                .distinct()
                // terminal
                .forEach(System.out::println); // method reference
    }

    @Test//(expected = IllegalStateException.class)
    public void yolo() {
        Stream<String> letsReuseIt = Stream.of("ene", "due", "rike", "fake", "ene", "due", "rike", "fake");
        letsReuseIt.sorted(String::compareTo).forEach(System.out::println);

        // exception expected here:
//        letsReuseIt
//                .distinct()
//                .forEach(System.out::println);
    }

    @Test
    public void findLongest() {
        String longest = createLoremIpsumStream()
                .reduce("",
                        (s1, s2) -> (s2.length() > s1.length()) ? s2 : s1
                );
        System.out.println("Longest line: " + longest + '(' + longest.length() + ')');
    }

    @Test
    public void split() {
        createLoremIpsumStream()
                .flatMap(new Function<String, Stream<?>>() {
                    @Override
                    public Stream<?> apply(String s) {
                        return Stream.of(s.split("\\W+"));
                    }
                })
                .forEach(System.out::println);
    }

    @Test
    public void splitAndCountWords() {
        Map<String, Integer> wordCount =
        createLoremIpsumStream()
                .flatMap(s -> Stream.of(s.split("\\W+")))
                .sorted()
                .collect(Collectors.toMap(w -> w.toLowerCase(), w -> 1, Integer::sum))
                ;
        System.out.println(wordCount);
    }

    @Test
    public void asyndeton() {
        String asyndeton = Stream.of("Przybyłem", "zobaczyłem", "zwyciężyłem")
                .collect(Collectors.joining(", "));
        System.out.println(asyndeton);
    }

    private Stream<String> createLoremIpsumStream() {
        return Stream.of(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit paradigma",
                "Periculo mitigandam paradigma",
                "Mauris a est pharetra, facilisis odio et, pulvinar nisi",
                "Sed in neque finibus, paradigma tempus nisl ac, bibendum leo convallis",
                "Quisque eu elit ac lacus luctus convallis",
                "Nullam eget tortor tincidunt, facilisis arcu nec, convallis turpis Morbi ut velit eleifend, molestie nulla eu, porta risus",
                "Praesent vestibulum orci ac dui imperdiet rutrum",
                "Sed a lorem luctus, aliquam risus sit amet, blandit convallis risus",
                "Quisque vel arcu bibendum, tempus lectus eu, malesuada tortor",
                "Aenean finibus ante et lorem finibus suscipit",
                "Aenean convallis leo in viverra paradigma viverra",
                "Suspendisse quis nisi a sapien mollis aliquam",
                "Vivamus ultricies leo ac aliquet gravida",
                "Vivamus  euismod elit ac aliquet gravida euismod elit",
                "Curabitur paradigma euismod elit a lectus malesuada imperdiet",
                "Ut a eros dictum, ultricies paradigma ligula eu, convallis tortor",
                "Sed in erat laoreet, placerat dolor ut paradigma, aliquam tellus",
                "Sed vel purus sed risus aliquet ultricies sit amet paradigma nec mi",
                "Ut efficitur lacus blandit, bibendum justo non, imperdiet metus paradigma",
                "Sed nec mi ultrices, tristique massa in, tincidunt sapien"
        );
    }

}