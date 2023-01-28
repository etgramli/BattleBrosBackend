package de.etgramli.battlebros.util;

import org.springframework.lang.NonNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CollectionUtil {
    private CollectionUtil() {}

    @NonNull
    public static String join(@NonNull final CharSequence delimiter, @NonNull final EnumSet<?> values) {
        return values.stream().map(Enum::toString).collect(Collectors.joining(delimiter));
    }

    @NonNull
    public static <K, V> List<Map.Entry<K, V>> listFromMap(@NonNull final Map<K, V> map) {
        return map.entrySet().stream().toList();
    }
}
