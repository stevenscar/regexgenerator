package it.scarano.regexgenerator;

import static it.scarano.regexgenerator.SimpleTokenType.LETTER;
import static it.scarano.regexgenerator.SimpleTokenType.DIGIT;
import static java.lang.Character.DECIMAL_DIGIT_NUMBER;
import static java.lang.Character.UPPERCASE_LETTER;
import static lombok.AccessLevel.NONE;

import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = NONE)
public class AlphaNumericRegexGenerator implements RegexGenerator {
  public static final RegexGenerator INSTANCE = new AlphaNumericRegexGenerator();
  public static final String START_WITH_LETTER_REGEX = "^[A-Z].*";

  public String generateRegex(List<String> inputList) {

    checkNullOrEmpty(inputList);
    List<RegexToken> regexTokenList = new ArrayList<>();

    for(int inputIndex = 0; inputIndex < inputList.size(); inputIndex++) {

      String input = inputList.get(inputIndex);
      checkNullOrBlank(input);
      checkStartsWithLetter(input);

      int tokenIndex = 0;
      int regexTokenCharCounter = 0;
      char[] currentInputCharArr = input.toCharArray();
      TokenType previousTokenType = null;
      for(int charIndex = 0; charIndex < currentInputCharArr.length; charIndex++) {

        char currentChar = currentInputCharArr[charIndex];
        TokenType tokenType = getTokenType(currentChar);

        boolean isLastChar = charIndex == currentInputCharArr.length - 1;
        boolean isSameTypeAsPrevious = tokenType.equals(previousTokenType);
        boolean isLastCharAndDifferentFromPreviousType = !isSameTypeAsPrevious && isLastChar;
        boolean isLastCharAndSameAsPreviousType        = isSameTypeAsPrevious && isLastChar;

        boolean doCreateOrUpdate = previousTokenType != null && !isSameTypeAsPrevious || isLastChar;

        if(doCreateOrUpdate) {
          if(inputIndex == 0) {
            createRegexToken(previousTokenType, regexTokenList, isLastCharAndSameAsPreviousType ? regexTokenCharCounter + 1 : regexTokenCharCounter);
            if(isLastCharAndDifferentFromPreviousType) {
              createRegexToken(tokenType, regexTokenList, 1);
            }
          } else {
            updateRegexToken(regexTokenList, tokenIndex, isLastCharAndSameAsPreviousType ? regexTokenCharCounter + 1 : regexTokenCharCounter);
            if(isLastCharAndDifferentFromPreviousType) {
              updateRegexToken(regexTokenList, tokenIndex + 1, 1);
            }
          }
          regexTokenCharCounter = 0;
          tokenIndex++;
        }
        regexTokenCharCounter++;
        previousTokenType = tokenType;
      }
    }
    return computeRegexTokenList(regexTokenList);
  }

  private TokenType getTokenType(char currentChar) {
    switch(Character.getType(currentChar)) {
      case UPPERCASE_LETTER:
        return LETTER;
      case DECIMAL_DIGIT_NUMBER:
        return DIGIT;
      default:
        throw new IllegalArgumentException(String.format("Character %s not allowed", currentChar));
    }
  }

  public String generateRegex(String inputToMatch) {
    return generateRegex(List.of(inputToMatch));
  }

  private void checkStartsWithLetter(String input) {
    if(!input.matches(START_WITH_LETTER_REGEX)) {
      throw new IllegalArgumentException(String.format("Invalid input %s. It should start with a letter [A-Z]", input));
    }
  }

  private void checkNullOrBlank(String input) {
    if(input == null || input.isBlank()) {
      throw new IllegalArgumentException("Input cannot be null or blank");
    }
  }

  private void checkNullOrEmpty(List<String> inputList) {
    if(inputList == null || inputList.isEmpty()) {
      throw new IllegalArgumentException("Input list cannot be empty or null");
    }
  }

  private void createRegexToken(TokenType tokenType, List<RegexToken> regexTokenList, int charCounter) {
    RegexToken regexToken = new RegexToken(tokenType, charCounter, charCounter);
    regexTokenList.add(regexToken);
  }

  private void updateRegexToken(List<RegexToken> regexTokenList, int tokenIndex, int charCounter) {
    if(tokenIndex > regexTokenList.size() - 1) {
      throw new IllegalArgumentException("The items in the input list don't follow the same letter/number sequence");
    }
    RegexToken regexToken = regexTokenList.get(tokenIndex);
    regexToken.updateMinMax(charCounter);
  }

  private String computeRegexTokenList(List<RegexToken> tokenList) {
    return tokenList.stream()
            .map(RegexToken::compute)
            .reduce("", (accumulator, current) -> accumulator + current);
  }

}
