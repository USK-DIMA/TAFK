package ru.uskov.dmitry.tafk.lab1;

/**
 * Created by Dmitry on 16.09.2016.
 */
public class SystemSymbolInLine {

    SystemSymbol systemSymbol;

    private int index;

    public SystemSymbolInLine(SystemSymbol systemSymbol, int index) {
        this.systemSymbol = systemSymbol;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SystemSymbol getSystemSymbol() {
        return systemSymbol;
    }
}
