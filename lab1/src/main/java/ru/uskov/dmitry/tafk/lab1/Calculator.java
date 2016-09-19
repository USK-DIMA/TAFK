package ru.uskov.dmitry.tafk.lab1;

import ru.uskov.dmitry.tafk.lab1.exception.IllegalArgumentCountException;
import ru.uskov.dmitry.tafk.lab1.exception.NoDeffCustomMethodException;
import ru.uskov.dmitry.tafk.lab1.exception.ParenthesisNumberException;
import ru.uskov.dmitry.tafk.lab1.methods.CustomMethod;
import ru.uskov.dmitry.tafk.lab1.methods.PowMethod;
import ru.uskov.dmitry.tafk.lab1.methods.SqrtMethod;

import java.util.*;

/**
 * Created by Dmitry on 11.09.2016.
 */
public class Calculator {

    private static final SystemSymbol[] SYSTEM_SYMBOL = SystemSymbol.getAllSymbols();

    private static List<CustomMethod> customMethodList = initCustomMethodList();

    private static List<CustomMethod> initCustomMethodList() {
        List<CustomMethod> customMethodList = new ArrayList<>();
        customMethodList.add(new PowMethod());
        customMethodList.add(new SqrtMethod());
        return customMethodList;
    }

    public String calculate(String input) throws IllegalArgumentCountException, NoDeffCustomMethodException, ParenthesisNumberException {
        input=input.replace(" ","");
        int openingParenthesisNum = 0;
        int closingParenthesisNum = 0;
        int firstOpeningParanthesisIndex = -1; //индекс первой отркывающей скобки
        for(int i=0; i<input.length(); i++){
            switch (input.charAt(i)){
                case '(':
                    if(openingParenthesisNum==closingParenthesisNum){
                        firstOpeningParanthesisIndex=i;
                    }
                    openingParenthesisNum++;
                    break;
                case ')':
                    closingParenthesisNum++;

                    if(openingParenthesisNum==closingParenthesisNum){
                        CustomMethod customMethod = getCustomMethod(input, firstOpeningParanthesisIndex);
                        String sub;
                        int customMethodNameLength=0;
                        if(customMethod!=null){
                            sub = customMethod.calculate(input.substring(firstOpeningParanthesisIndex+1, i), this);
                            customMethodNameLength = customMethod.getName().length();
                        }else {
                            sub = calculate(input.substring(firstOpeningParanthesisIndex+1, i));
                        }
                        //заменяем кусок входной строки.НО НАДО НЕ ЗАБЫТЬ ПРО итератор i!
                        input = replace(input, firstOpeningParanthesisIndex-customMethodNameLength, i, sub);
                        i = i - ((customMethodNameLength+(i-firstOpeningParanthesisIndex))-sub.length())-1;
                    }
                    break;
            }
        }


        /**По идее, ото всех скобок избавились и ото всех функций.*/
        if(find(input, new char[]{'(', ')'})){
            throw new ParenthesisNumberException("Number of Opened Parenthesis != number of Cloused Parenthesis");
        }

        List<SystemSymbolInLine> systemIndex = getSystemIndex(input);
        List<SystemSymbolInLine> systemIndexSortByPriority = new ArrayList<>(systemIndex);

        Collections.sort(systemIndexSortByPriority, new Comparator<SystemSymbolInLine>() {
            @Override
            public int compare(SystemSymbolInLine o1, SystemSymbolInLine o2) {
                return (o2.getSystemSymbol().getPriority() - o1.getSystemSymbol().getPriority());
            }
        });

        LinkedList<Double> values = getValuesFromInput(input, systemIndex);
        for(int i=0; i<systemIndexSortByPriority.size(); i++){
            int number = systemIndexSortByPriority.get(i).number;
            Double v1 = values.get(number);
            Double v2 = values.get(number+1);
            Double ans = calculate(systemIndexSortByPriority.get(i).getSystemSymbol().getSymbol(), v1, v2);
            values.remove(number);
            values.remove(number);
            incrementNumber(systemIndex, number);
            values.add(number, ans);
        }

        if(values.size()!=1){
            throw new RuntimeException("Something wrong");
        } else {
            return values.get(0).toString();
        }

    }


    private void incrementNumber(List<SystemSymbolInLine> systemIndex, int number) {
        for(int i=number+1; i<systemIndex.size(); i++){
            systemIndex.get(i).incrementNumber();
        }
    }

    private Double calculate(char symbol, Double v1, Double v2) {
        switch (symbol){
            case '-': return v1-v2;
            case '+': return v1+v2;
            case '*': return v1*v2;
            case '/': return v1/v2;
            case '^': return Math.pow(v1, v2);
            default: throw new RuntimeException("Неверный арифметический знак: "+ symbol);
        }
    }

    /**
     * Достаёт из входной строки только числа. systemIndex содержит разделители
     * @param input
     * @param systemIndex отсортированн по индексу!
     * @return
     */
    private LinkedList<Double> getValuesFromInput(String input, List<SystemSymbolInLine> systemIndex) {
        LinkedList<Double> values = new LinkedList<>();
        int stastIndex = 0;
        for(int i=0; i<systemIndex.size(); i++){
            values.add(Double.valueOf(input.substring(stastIndex, systemIndex.get(i).index)));
            stastIndex=systemIndex.get(i).index+1;
        }
        values.add(Double.valueOf(input.substring(stastIndex, input.length())));
        return values;
    }


    /**
     * Возвращает индексы всех системных символов кроме тех минусов, которые относятся к числу например: 5*-3 будет возвращён только индекс 1
     * @param input
     * @return
     */
    private List<SystemSymbolInLine> getSystemIndex(String input) {
        List<SystemSymbolInLine> indexes = new ArrayList<>();
        int number=0;
        for(int i=0; i<input.length(); i++){
            char ch = input.charAt(i);
            if(isSystemSymbol(ch)){
                if(i==0 || (indexes.size()> 0 && indexes.get(indexes.size()-1).getIndex()==i-1)){
                    if(ch!='-'){
                        throw new RuntimeException("Ожидается знак -. Пришёл: "+ ch);
                    }
                } else {
                    indexes.add(new SystemSymbolInLine(SystemSymbol.getByChar(ch), i, number));
                    number++;//номер арифм знака по счёту
                }
            }
        }
        return indexes;
    }


    private boolean find(String input, char[] symbols) {
        for(int i=0; i<input.length(); i++){
            char ch = input.charAt(i);
            for(int j=0; j<symbols.length; j++){
                if(ch == symbols[j]){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Анализиует входну строку (input) и индекс открывающейся скобки в строке  и возвращает кастомный метод, если это скобка относиться к какому-то кастомнумоу методу
     * иначе null;
     * @return
     */
    private CustomMethod getCustomMethod(String input, int openingParanthesisIndex) throws NoDeffCustomMethodException {
        int index = findNearestSystemSymbolLeft(input, openingParanthesisIndex);
        if(index==openingParanthesisIndex-1){
            return null;
        }
        String maybeCustomMethodName = input.substring(index+1, openingParanthesisIndex);
        for(int i=0; i<customMethodList.size(); i++){
            CustomMethod customMethod = customMethodList.get(i);
            if(maybeCustomMethodName.equals(customMethod.getName())){
                return customMethod;
            }
        }
        throw new NoDeffCustomMethodException("Not found custom method with name '"+maybeCustomMethodName+"'");
    }

    /**
     * Ишет ближайший служебный символ в тексте input слева от символа с индексом targetIndex
     * @param input
     * @param targetIndex
     * @return
     */
    private int findNearestSystemSymbolLeft(String input, int targetIndex) {
        for(int i=targetIndex-1; i>=0; i--){
            if(isSystemSymbol(input.charAt(i))){
                return i;
            }
        }
        return -1;
    }

    private int findNearestSystemSymbolRight(String input, int targetIndex) {
        for(int i=0; i<input.length(); i++){
            if(isSystemSymbol(input.charAt(i))){
                return i;
            }
        }
        return -1;
    }


    /**
     * Определяет, является ли символ служебным
     * @param ch
     * @return
     */
    private boolean isSystemSymbol(char ch) {
        for(int i=0; i<SYSTEM_SYMBOL.length; i++){
            if(ch ==SYSTEM_SYMBOL[i].getSymbol()){
                return true;
            }
        }
        return false;
    }

    /**
     * Заменяет в строке str все символы, начиная с begin по end (включительно) на строку newSubString
     * @param str исходная строка
     * @param begin индекс первого символа, которую надо заменить
     * @param end последний индекс строки, который надо заменить
     * @param newSubString
     * @return
     */
    private String replace(String str, int begin, int end, String newSubString) {
        int newLength = str.length()-(end-begin)-1+newSubString.length();
        StringBuilder newStr = new StringBuilder(newLength);
        newStr.append(str.substring(0, begin));
        newStr.append(newSubString);
        newStr.append(str.substring(end+1));
        return newStr.toString();
    }

    public static class SystemSymbolInLine {

        SystemSymbol systemSymbol;

        private int index;

        private int number;

        public SystemSymbolInLine(SystemSymbol systemSymbol, int index, int number) {
            this.systemSymbol = systemSymbol;
            this.index = index;
            this.number = number;
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

        public int getNumber() {
            return number;
        }

        public void incrementNumber(){
            number--;
        }
    }

}
