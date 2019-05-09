package com.example.accountservice.component.client.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utils for parsing strings like 1,2,3,4 or 1-4, but not their composition.
 */
public class IdRangeParsingUtils {

    private final static Pattern RANGE_PATTERN = Pattern.compile("^(\\d+)-(\\d+)$");

    private final static Pattern SIMPLE_VALUES_PATTERN = Pattern.compile("^\\d+(,\\d+)*$");

    public interface IdHolder {
        Integer getRandomId();
    }

    public static class IdRange implements IdHolder {
        private Integer lowerBound;

        private Integer upperBound;

        public IdRange(Integer lowerBound, Integer upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public Integer getLowerBound() {
            return lowerBound;
        }

        public Integer getUpperBound() {
            return upperBound;
        }

        @Override
        public Integer getRandomId() {
            return ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
        }
    }

    public static class IdEnumeration implements IdHolder {

        private List<Integer> enumeration;

        public IdEnumeration(List<Integer> enumeration) {
            this.enumeration = enumeration;
        }

        public List<Integer> getEnumeration() {
            return enumeration;
        }

        @Override
        public Integer getRandomId() {
            return ThreadLocalRandom.current().nextInt(0, enumeration.size());
        }
    }

    public static IdHolder parseIdRangeString(String idRangeString) {
        try {
            Matcher rangeMatcher = RANGE_PATTERN.matcher(idRangeString);
            if (rangeMatcher.matches()) {
                return createIdRange(rangeMatcher);
            }

            Matcher simpleValuesMatcher = SIMPLE_VALUES_PATTERN.matcher(idRangeString);
            if (simpleValuesMatcher.matches()) {
                return createIdEnumeration(idRangeString);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid id range string: " + idRangeString);
        }

        throw new IllegalArgumentException("Invalid id range string: " + idRangeString);
    }

    private static IdHolder createIdRange(Matcher rangeMatcher) {
        Integer lowerBound = Integer.valueOf(rangeMatcher.group(1));
        Integer upperBound = Integer.valueOf(rangeMatcher.group(2));
        return new IdRange(lowerBound,upperBound);
    }

    private static IdHolder createIdEnumeration(String idRangeString) {
        List<Integer> enumeration = Stream.of(idRangeString.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        return new IdEnumeration(enumeration);
    }

    private IdRangeParsingUtils() {
    }
}
