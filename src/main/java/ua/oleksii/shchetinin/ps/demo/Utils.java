package ua.oleksii.shchetinin.ps.demo;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

@UtilityClass
public class Utils {

    public static <T> Stream<T> safeStream(Collection<T> collection) {
        if (Objects.isNull(collection)) {
            return Stream.empty();
        }
        return collection.stream();
    }
}
