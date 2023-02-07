package it.scarano.regexgenerator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static it.scarano.regexgenerator.RegexGenerator.*;

class RegexTokenTest {

    @Test
    @DisplayName("Should return single frequency regex when min and max equal one")
    void shouldReturnSingleFrequencyRegex_WhenMinAndMaxEqualOne() {

        int minMax = 1;
        RegexToken regexToken = new RegexToken(SimpleTokenType.DIGIT, minMax, minMax);

        String result = regexToken.compute();

        Assertions.assertEquals("\\d", result);

    }

    @Test
    @DisplayName("Should return fixed frequency regex when min and max are equals")
    void shouldReturnFixedFrequencyRegex_WhenMinAndMaxAreEquals() {

        int minMax = 2;
        RegexToken regexToken = new RegexToken(SimpleTokenType.LETTER, minMax, minMax);

        String result = regexToken.compute();

        Assertions.assertEquals(String.format("[A-Z]{%s}", minMax), result);

    }

    @Test
    @DisplayName("Should return range frequency regex when min lesser than max")
    void shouldReturnRangeFrequencyRegex_WhenMinLesserThanMax() {

        int min = 2;
        int max = 3;
        RegexToken regexToken = new RegexToken(SimpleTokenType.DIGIT, min, max);

        String result = regexToken.compute();

        Assertions.assertEquals(String.format("\\d{%d,%d}", min, max), result);

    }

    @Test
    @DisplayName("Should throw exception when min greater than max")
    void shouldThrowException_WhenMinGreaterThanMax() {

        int min = 3;
        int max = 2;
        RegexToken regexToken = new RegexToken(SimpleTokenType.LETTER, min, max);

        Assertions.assertThrows(IllegalStateException.class, regexToken::compute);

    }

    @Test
    @DisplayName("Should update min when value lesser than min")
    void shouldUpdateMin_WhenValueLesserThanMin() {

        int startingMin = 2;
        int startingMax = 4;
        RegexToken regexToken = new RegexToken(SimpleTokenType.DIGIT, startingMin, startingMax);

        int newMin = 1;
        regexToken.updateMinMax(newMin);

        Assertions.assertEquals(newMin, regexToken.getMin());
        Assertions.assertEquals(startingMax, regexToken.getMax());
    }

    @Test
    @DisplayName("Should update max when value greater than max")
    void shouldUpdateMax_WhenValueGreaterThanMax() {

        int startingMin = 1;
        int startingMax = 2;
        RegexToken regexToken = new RegexToken(SimpleTokenType.LETTER, startingMin, startingMax);

        //when
        int newMax = 5;
        regexToken.updateMinMax(newMax);

        Assertions.assertEquals(startingMin, regexToken.getMin());
        Assertions.assertEquals(newMax, regexToken.getMax());

    }

    @Test
    @DisplayName("Should not update when value equal to max and min")
    void shouldNotUpdate_WhenValueEqualToMaxAndMin() {

        int startingMin = 2;
        int startingMax = 2;
        RegexToken regexToken = new RegexToken(SimpleTokenType.LETTER, startingMin, startingMax);

        int value = 2;
        regexToken.updateMinMax(value);

        Assertions.assertEquals(startingMin, regexToken.getMin());
        Assertions.assertEquals(startingMax, regexToken.getMax());

    }

}