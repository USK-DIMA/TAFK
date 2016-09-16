package ru.uskov.dmitry.tafk.lab1.methods;

import ru.uskov.dmitry.tafk.lab1.Calculator;
import ru.uskov.dmitry.tafk.lab1.exception.IllegalArgumentCountException;
import ru.uskov.dmitry.tafk.lab1.exception.NoDeffCustomMethodException;
import ru.uskov.dmitry.tafk.lab1.exception.ParenthesisNumberException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dmitry on 11.09.2016.
 */

/**
 * Общий класс-родитель для всех кастомных методов (например: sqrt, pow, log и любые другие)
 * При добавлении нового метода не забудьте зарегистрировать его в самом калькуляторе (method Calculator.initCustomMethodList)
 */
abstract public class CustomMethod {

    public abstract String getName();

    public abstract int getArgumentCount();

    public abstract char getSeparator();

    /**
     * Содиржит общую логику для всех кастомных методов, а именно парсинг аргументов и валидация их кол-ва.
     * Сама логика работы кастомного метода определяется в protected abstract double calculate(double[] args);
     * @param inputString входные аргументы функции. Могут содержать в себе и выражения
     * @param calculator -- калькулятор, который будет вычислять те аргументы кастомного метода, которые переданны в виде выражения
     * @return
     * @throws IllegalArgumentCountException
     */
    public String calculate(String inputString, Calculator calculator) throws IllegalArgumentCountException, NoDeffCustomMethodException, ParenthesisNumberException {
        String[] args = getArgs(inputString, calculator);
        double[] doubleArgs = convertToDouble(args);

        return String.valueOf(calculate(doubleArgs));
    }

    private double[] convertToDouble(String[] args) {
        double[] argsNum = new double[args.length];
        for(int i=0; i<args.length; i++){
            argsNum[i] = Double.valueOf(args[i]);
        }
        return argsNum;
    }


    /**
     * Передаёт аргументы функции в 'чистом' виде, т.е. уже без всяких выражений. только голые числа
     * @param inputString
     * @param calculator
     * @throws IllegalArgumentCountException если передано неверное кол-во аргументов
     * @return
     */
    private String[] getArgs(String inputString, Calculator calculator) throws IllegalArgumentCountException, NoDeffCustomMethodException, ParenthesisNumberException {
        List<String> argsList = new ArrayList<>();
        int startArgIndex = 0;
        //необходимо учесть, что как аргументы кастомного метода могут передать и дургие функции, у которых есть свои сепараторы.
        //Поэтому просто тупо split по сепаратору делать нельзя.
        int openingParenthesisNum = 0;
        int clousingParenthesisNum = 0;
        for(int i=0; i<inputString.length(); i++){
            switch (inputString.charAt(i)){
                case '(': openingParenthesisNum++;
                    break;
                case ')':clousingParenthesisNum++;
                    break;
            }

            if(inputString.charAt(i)==getSeparator() && (openingParenthesisNum==clousingParenthesisNum)){
                argsList.add(inputString.substring(startArgIndex, i));
                startArgIndex = i+1;
            }
        }
        argsList.add(inputString.substring(startArgIndex, inputString.length()));
        //Проверяем кол-во аргументов
        if(argsList.size()!=getArgumentCount()){
            throw new IllegalArgumentCountException(getArgumentCount(), argsList.size(), getName()+"("+inputString+")");
        }
        //преобразуем аргументы функции, если есть выражения с помощью калькулятора
        String[] argsArray = new String[argsList.size()];
        for(int i=0; i<argsList.size(); i++){
            argsArray[i] = calculator.calculate(argsList.get(i));
        }
        return argsArray;
    }

    protected abstract double calculate(double[] args);
}
