package me.tabinol.secuboid.core.utilities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "land",
            "-minus",
            "minus-"
    })
    void when_unvalid_then_return_false(String arg) {
        assertFalse(NameUtil.validateName(arg));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "élève",
            "abatoire",
            "endroit-01"
    })
    void when_valid_then_return_true(String arg) {
        assertTrue(NameUtil.validateName(arg));
    }
}