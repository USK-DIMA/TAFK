package ru.uskov.dmitry.tafk.lab1;

/**
 * Created by Dmitry on 11.09.2016.
 */
public class PowMethod extends CustomMethod {
    @Override
    public String getName() {
        return "pow";
    }

    @Override
    public int getArgumentCount() {
        return 2;
    }

    @Override
    public char getSeparator() {
        return ',';
    }

    @Override
    public double calculate(double[] args) {
        return Math.pow(args[0], args[1]);
    }
}
