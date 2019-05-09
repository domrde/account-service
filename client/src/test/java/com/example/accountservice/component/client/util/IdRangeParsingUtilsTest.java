package com.example.accountservice.component.client.util;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IdRangeParsingUtilsTest {

    private static final int LOWER_BOUND = 1;

    private static final int UPPER_BOUND = 100;

    @Test
    public void rangeShouldBeReturnedForRangeBasedInputString() {
        String rangeInputString = String.format("%d-%d", LOWER_BOUND, UPPER_BOUND);

        IdRangeParsingUtils.IdHolder idHolder = IdRangeParsingUtils.parseIdRangeString(rangeInputString);

        assertTrue(idHolder instanceof IdRangeParsingUtils.IdRange);
        assertEquals(LOWER_BOUND, ((IdRangeParsingUtils.IdRange) idHolder).getLowerBound().intValue());
        assertEquals(UPPER_BOUND, ((IdRangeParsingUtils.IdRange) idHolder).getUpperBound().intValue());
    }

    @Test
    public void enumerationShouldBeReturnedForEnumerationBasedInputString() {
        List<Integer> inputEnumeration = IntStream.rangeClosed(LOWER_BOUND, UPPER_BOUND).boxed().collect(Collectors.toList());
        String inputEnumerationString = inputEnumeration.stream().map(String::valueOf).collect(Collectors.joining(","));

        IdRangeParsingUtils.IdHolder idHolder = IdRangeParsingUtils.parseIdRangeString(inputEnumerationString);

        assertTrue(idHolder instanceof IdRangeParsingUtils.IdEnumeration);
        assertEquals(inputEnumeration, ((IdRangeParsingUtils.IdEnumeration) idHolder).getEnumeration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionShouldBeThrownForMixedIdInputString() {
        IdRangeParsingUtils.parseIdRangeString("1, 2, 3-100");
    }

}
