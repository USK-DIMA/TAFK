package ru.uskov.dmitry.tafk.lab1.exception;

/**
 * Created by Dmitry on 11.09.2016.
 */

/**
 * Выбрасывается, если у кастомной функции неверное количество аргументов
 */
public class IllegalArgumentCountException extends Exception {

    public IllegalArgumentCountException(int expectedCount, int realCount, String example) {
        super("Illegar argument count. Expected: "+ expectedCount+". Real: "+realCount+"Your using: "+example);
    }
}
