package com.monad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void testMainMethod() {
        // Simple test to verify the project structure works
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }
}

