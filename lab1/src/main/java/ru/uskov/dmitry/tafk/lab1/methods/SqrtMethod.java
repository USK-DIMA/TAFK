package ru.uskov.dmitry.tafk.lab1.methods;

/**
 * Created by Dmitry on 16.09.2016.
 */
public class SqrtMethod extends CustomMethod {
    @Override
    public String getName() {
        return "sqrt";
    }

    @Override
    public int getArgumentCount() {
        return 1;
    }

    @Override
    public char getSeparator() {
        return ',';
    }

    @Override
    protected double calculate(double[] args) {
        return Math.sqrt(args[0]);
    }
}
