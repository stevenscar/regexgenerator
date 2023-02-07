package it.scarano.regexgenerator;

import static it.scarano.regexgenerator.ComplexStringListParameterResolver.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@ExtendWith(ComplexStringListParameterResolver.class)
class AlphaNumericRegexGeneratorTest {

  private static final String TEST_CASE_1_REGEX       = "[A-Z]{2}\\d{3}[A-Z]{2}";
  private static final String TEST_CASE_2_REGEX       = "[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]";
  private static final String TEST_CASE_3_REGEX       = "[A-Z]{2}\\d{3,5}";
  private static final String TEST_CASE_4_REGEX       = "[A-Z]{1,2}\\d{3,5}[A-Z]{1,2}";

  @Test
  @DisplayName("Should generate valid regex - Test Case 1")
  void shouldGenerateValidRegex_TestCase1() {

    List<String> testList = List.of("AB123ZZ",
                                    "BB742TG",
                                    "CF678HG");

    String result = AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList);

    assertEquals(TEST_CASE_1_REGEX, result);
    testList.forEach( (input) ->
            assertTrue(input.matches(TEST_CASE_1_REGEX))
    );
  }

  @Test
  @DisplayName("Should generate valid regex - Test Case 2")
  void shouldGenerateValidRegex_TestCase2() {

    String test = "TNTTST80A01F205E";

    String result = AlphaNumericRegexGenerator.INSTANCE.generateRegex(test);

    assertEquals(TEST_CASE_2_REGEX, result);
    assertTrue(test.matches(TEST_CASE_2_REGEX));

  }

  @Test
  @DisplayName("Should generate valid regex - Test Case 3")
  void shouldGenerateValidRegex_TestCase3() {

    List<String> testList = List.of("AA123",
                                    "BA1234",
                                    "AB12345");

    String result = AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList);

    assertEquals(TEST_CASE_3_REGEX, result);
    testList.forEach( (input) ->
            assertTrue(input.matches(TEST_CASE_3_REGEX))
    );

  }

  @Test
  @DisplayName("Should generate valid regex - Test Case 4")
  void shouldGenerateValidRegex_TestCase4() {

    List<String> testList = List.of("A123XY",
                                    "BA1234ZT",
                                    "AB12345B");

    String result = AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList);

    assertEquals(TEST_CASE_4_REGEX, result);
    testList.forEach( (input) ->
            assertTrue(input.matches(TEST_CASE_4_REGEX))
    );

  }


  @Test
  @DisplayName("Should generate valid regex - Complex Case")
  void shouldGenerateValidRegex_ComplexCase(@ComplexStringList List<String> testList) {

    String result = AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList);

    assertEquals(TEST_CASE_COMPLEX_REGEX, result);
    testList.forEach( (input) ->
            assertTrue(input.matches(TEST_CASE_COMPLEX_REGEX))
    );

  }

  @ParameterizedTest
  @NullAndEmptySource
  @DisplayName("Should throw exception when input list is empty or null")
  void shouldThrowException_WhenInputListIsEmptyOrNull(List<String> testList) {

    assertThrows( IllegalArgumentException.class, () -> AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList));

  }

  @Test
  @DisplayName("Should throw exception when input contains symbol")
  void shouldThrowException_WhenInputContainsSymbol() {

    List<String> testList = List.of("CA3=Y",
                                    "BA1?T",
                                    "D123.B");

    assertThrows( IllegalArgumentException.class, () -> AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList));

  }

  @Test
  @DisplayName("Should throw exception when input is null")
  void shouldThrowException_WhenInputIsNull() {

    List<String> testList = new ArrayList<>();
    testList.add(null);

    assertThrows( IllegalArgumentException.class, () -> AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList));

  }

  @Test
  @DisplayName("Should throw exception when input is empty")
  void shouldThrowException_WhenInputIsEmpty() {

    List<String> testList = List.of("");

    assertThrows( IllegalArgumentException.class, () -> AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList));

  }

  @Test
  @DisplayName("Should throw exception when input contains lowercase letter")
  void shouldThrowException_WhenInputContainsLowerCaseLetter() {

    List<String> testList = List.of("Lz984XY",
                                    "BAc1234eT",
                                    "kO134T");

    assertThrows( IllegalArgumentException.class, () -> AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList));

  }

  @Test
  @DisplayName("Should throw exception when input starts with number")
  void shouldThrowException_WhenInputStartsWithNumber() {

    List<String> testList = List.of("9A5DF",
                                    "89BA1234ZT",
                                    "12345B1TF");

    assertThrows( IllegalArgumentException.class, () -> AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList));

  }

  @Test
  @DisplayName("Should throw exception when items in input do not follow the same sequence")
  void shouldThrowException_WhenItemsInInputDoNotFollowTheSameSequence() {

    List<String> testList = List.of("AB11B",
                                    "AB1A1");

    assertThrows( IllegalArgumentException.class, () -> AlphaNumericRegexGenerator.INSTANCE.generateRegex(testList));

  }

}