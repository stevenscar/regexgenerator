package it.scarano.regexgenerator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
enum SimpleTokenType implements TokenType {

  LETTER("[A-Z]"),
  DIGIT("\\d");

  private final String value;

}