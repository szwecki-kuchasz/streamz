package chapter.devops.javastreams.lambdaz;

import java.util.function.BinaryOperator;

public class MaybeReusedAccumulator implements BinaryOperator<String> {
    @Override
    public String apply(String s1, String s2) {
        return s2.length() > s1.length() ? s2 : s1;
    }
}
