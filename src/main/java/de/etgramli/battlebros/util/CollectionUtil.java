package de.etgramli.battlebros.util;

import org.springframework.lang.NonNull;

import java.util.EnumSet;
import java.util.stream.Collectors;

public final class CollectionUtil {
    private CollectionUtil() {}

    @NonNull
    public static String join(@NonNull final CharSequence delimiter, @NonNull final EnumSet<?> values) {
        return values.stream().map(Enum::toString).collect(Collectors.joining(delimiter));
    }
}
