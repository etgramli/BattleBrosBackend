package de.etgramli.battlebros.util;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * Creates a list of Map.Entry baked from a Map. The entries are copied from the map.
     * @param map The map to be converted to list. Must not be null.
     * @return A list, not null.
     * @param <K> Type of the keys.
     * @param <V> Type of the values.
     */
    @NonNull
    public static <K, V> List<Map.Entry<K, V>> listFromMap(@NonNull final Map<K, V> map) {
        return map.entrySet().stream().map(Pair::of).collect(Collectors.toCollection(() -> new ArrayList<>(map.size())));
    }

    /**
     * Convert a collection of some subclass of Map.Entry (i.e. Pair) to a map.
     * @param entries Collection of entries, must not be null.
     * @return A map, must not be null.
     * @param <K> Type of keys.
     * @param <V> Type of values.
     */
    @NonNull
    public static <K, V> Map<K, V> mapFromCollection(@NonNull final Collection<? extends Map.Entry<K, V>> entries) {
        return entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b));
    }
}
