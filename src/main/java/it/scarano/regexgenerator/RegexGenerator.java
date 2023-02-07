package it.scarano.regexgenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

public interface RegexGenerator {

    String generateRegex(List<String> inputList);

    String generateRegex(String input);

    @Getter
    @AllArgsConstructor
    class RegexToken {

        private static final String FIXED_FREQUENCY_TEMPLATE = "{%d}";
        private static final String RANGE_FREQUENCY_TEMPLATE = "{%d,%d}";

        private final TokenType tokenType;
        private Integer min;
        private Integer max;

        public String compute() {

            StringBuilder resultBuilder = new StringBuilder().append(tokenType.getValue());

            if(min.equals(max)) {
                if(min > 1) {
                    resultBuilder.append(String.format(FIXED_FREQUENCY_TEMPLATE, min));
                }
            }
            else if(min < max) {
                resultBuilder.append(String.format(RANGE_FREQUENCY_TEMPLATE, min, max));
            }
            else {
                throw new IllegalStateException("Min cannot be bigger than max");
            }

            return resultBuilder.toString();
        }

        public void updateMinMax(int value) {
            if(value > max) {
                max = value;
            }
            if(value < min) {
                min = value;
            }
        }

    }

}
