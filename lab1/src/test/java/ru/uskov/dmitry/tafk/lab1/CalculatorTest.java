package ru.uskov.dmitry.tafk.lab1;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Dmitry on 11.09.2016.
 */
public class CalculatorTest {

    @Test
    public void calculate() throws Exception {
        assertEquals("27.0", new Calculator().calculate("2+pow(5, 2)"));
        assertEquals("18.0", new Calculator().calculate("2+pow(4, 2)"));
        assertEquals("18.0", new Calculator().calculate("2+pow(pow(2, 2), 2)"));
        assertEquals("18.0", new Calculator().calculate("2+pow(2^2, 2)"));
        assertEquals("18.0", new Calculator().calculate("2+2^pow(2, 2)"));
        assertEquals("-243.0", new Calculator().calculate("2+pow(sqrt(49), 2)*(14-19)"));
        assertEquals("4.0", new Calculator().calculate("-2^2"));
        assertEquals("0.0", new Calculator().calculate("-2+2"));
        assertEquals("-4.0", new Calculator().calculate("-2+(-2)"));
        assertEquals("-4.0", new Calculator().calculate("-2+-2"));
        assertEquals("256.0", new Calculator().calculate("-2^2^4"));
        assertEquals("-0.1762352359916263", new Calculator().calculate("(2+8-7*(6/sqrt(7)))*3/100"));
        assertEquals("0.1762352359916263", new Calculator().calculate("(2+8-7*(6/sqrt(7)))*3/-pow(10,2)"));
    }

}