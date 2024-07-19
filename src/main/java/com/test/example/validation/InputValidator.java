package com.test.example.validation;

import com.test.example.exception.InputValidationException;
import org.springframework.util.StringUtils;

public class InputValidator {

    private InputValidator() {
    }

    public static void validateStudentInput(String groupName, Integer ageLowerLimit) {
        if (ageLowerLimit != null && StringUtils.hasText(groupName)) {
            throw new InputValidationException(String.format(
                    "Cannot search students by age greater than %d and group name.", ageLowerLimit));
        }
    }

}
