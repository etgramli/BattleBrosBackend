package de.etgramli.battlebros.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class CollectionUtil {
    private CollectionUtil() {}

    public static String join(final CharSequence delimiter, final EnumSet<?> values) {
        final List<String> stringValues = new ArrayList<>(values.size());
        values.stream().map(Enum::toString).forEachOrdered(stringValues::add);

        return String.join(delimiter, stringValues);
    }
}
