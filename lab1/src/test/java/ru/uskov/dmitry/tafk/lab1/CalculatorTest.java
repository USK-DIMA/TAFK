package ru.uskov.dmitry.tafk.lab1;

import static org.junit.Assert.*;

/**
 * Created by Dmitry on 11.09.2016.
 */
public class CalculatorTest {
    @org.junit.Test
    public void replace() throws Exception {
        assertEquals("a+c", Calculator.replace("a+(q+r)", 2, 6, "c"));
    }

}