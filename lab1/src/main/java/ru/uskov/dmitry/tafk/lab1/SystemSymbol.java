package ru.uskov.dmitry.tafk.lab1;

/**
 * Created by Dmitry on 16.09.2016.
 */
public enum SystemSymbol {

    OPENING_PARENTHESIS('(', 1),
    CLOSING_PARENTHESIS(')', 1),
    MINUS('-', 2),
    PLAS('+', 2),
    MULTIPLICATION('*', 3),
    DIVISION('/', 3),
    POW('^', 4);

    private final static SystemSymbol[] allObjects = new SystemSymbol[]{
            OPENING_PARENTHESIS,
            CLOSING_PARENTHESIS,
            MINUS,
            PLAS,
            MULTIPLICATION,
            DIVISION,
            POW,
    };

    private final char symbol;

    private final int priority;

    SystemSymbol(char symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    public char getSymbol() {
        return symbol;
    }


    public int getPriority() {
        return priority;
    }


    public static SystemSymbol[] getAllSymbols(){
        return allObjects;
    }

    public static SystemSymbol getByChar(char ch) {
        for(int i=0; i<allObjects.length; i++){
            if(ch==allObjects[i].getSymbol()){
                return allObjects[i];
            }
        }
        return null;
    }
}
