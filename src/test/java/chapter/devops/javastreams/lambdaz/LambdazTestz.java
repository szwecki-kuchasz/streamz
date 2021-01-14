package chapter.devops.javastreams.lambdaz;

import org.junit.Test;

import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class LambdazTestz {

    private Stream<String> createTextz() {
        return Stream.of(
                "Niech się dzieje wola Nieba! Z nią się zawsze zgadzać trzeba.",
                "Zgoda-zgoda, a Bóg wtedy rękę poda",
                "Sąd sądem ale sprawiedliwość musi być po naszej stronie",
                "Siądź pod mym liściem a odpocznij sobie",
                "Day ać ja pobruszę a ty poczywaj"
        );
    }

    // Hey, Google: is java lambda equivalent to anonymous class?

    // https://www.tutorialspoint.com/differences-between-anonymous-class-and-lambda-expression-in-java
    // An anonymous class object generates a separate class file after compilation
    // that increases the size of a jar file
    // while a lambda expression is converted into a private method.

    @Test
    public void findLongest_Lambda() {
        String longest = createTextz()
                .reduce("",
                        (s1, s2) -> (s2.length() > s1.length()) ? s2 : s1
                );
        // no Java file
        // no class file
        System.out.println("Longest line by lambda: " + longest);
    }

    @Test
    public void findLongest_MethodReference() {
        String longest = createTextz()
                .reduce("",
                        this::findLongerString
                );
        // no Java file
        // no class file
        System.out.println("Longest line by method reference: " + longest);
    }

    private String findLongerString(String s1, String s2) {
        return s2.length() > s1.length() ? s2 : s1;
    }

    @Test
    public void findLongest_AnonymousClass() {
        String longest = createTextz()
                .reduce("",
                        new BinaryOperator<String>() {
                            @Override
                            public String apply(String s1, String s2) {
                                return s2.length() > s1.length() ? s2 : s1;
                            }
                        }
                );
        // no Java file
        // class file, yes
        System.out.println("Longest line by anonymous class: " + longest);
    }

    @Test
    public void findLongest_FirstCitizenClass() {
        String longest = createTextz()
                .reduce("",
                        new MaybeReusedAccumulator()
                );
        // Java and class
        System.out.println("Longest line by ordinary class: " + longest);
    }

}
