package ru.uskov.dmitry.tafk.lab1;

import ru.uskov.dmitry.tafk.lab1.exception.IllegalArgumentCountException;
import ru.uskov.dmitry.tafk.lab1.exception.NoDeffCustomMethodException;
import ru.uskov.dmitry.tafk.lab1.exception.ParenthesisNumberException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 11.09.2016.
 */
public class Calculator {

    public static final String FUNCTION_NAME_POW = "pow";

    private static final char[] SYSTEM_SYMBOL = {')', '(', '^', '*', '/', '+', '-'};

    private static List<CustomMethod> customMethodList = initCustomMethodList();

    private static List<CustomMethod> initCustomMethodList() {
        List<CustomMethod> customMethodList = new ArrayList<>();
        customMethodList.add(new PowMethod());
        return customMethodList;
    }

    public String getRevert(String input){
        StringBuilder builder = new StringBuilder(input.length());
        List<String> stack = new ArrayList<>();
        for(int i=0; i<input.length(); i++){
            //char ch = input
        }
        return null;
    }

    public String calculate(String input) throws IllegalArgumentCountException, NoDeffCustomMethodException, ParenthesisNumberException {
        int openingParenthesisNum = 0;
        int clousingParenthesisNum = 0;
        int firstOpeningParanthesisIndex = -1; //индекс первой отркывающей скобки
        for(int i=0; i<input.length(); i++){
            switch (input.charAt(i)){
                case '(':
                    if(openingParenthesisNum==0){
                        firstOpeningParanthesisIndex=i;
                    }
                    openingParenthesisNum++;
                    break;
                case ')':
                    clousingParenthesisNum++;

                    if(openingParenthesisNum==clousingParenthesisNum){
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
                        i = i-(i-firstOpeningParanthesisIndex+sub.length())-1; //// TODO: 11.09.2016 убедится, что здесь всё правильно
                    }
                    break;
            }
        }
        /**По идее, ото всех скобок избавились и ото всех функций.*/
        if(find(input, new char[]{'(', ')'})){
            throw new ParenthesisNumberException("Number of Opened Parenthesis != number of Cloused Parenthesis");
        }
        /**теперь производим обычные вычисления**/
        for(int i=0; i<SYSTEM_SYMBOL.length; i++){
        }
        return null;
    }


    private boolean find(String input, char[] strings) {
        for(int i=0; i<input.length(); i++){
            char ch = input.charAt(i);
            for(int j=0; j<strings.length; j++){
                if(ch == strings[j]){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Анализиует входну строку (Input) и индекс открывающейся скобки в строке  и возвращает кастомный метод, если это скобка относиться к какому-то кастомнумоу методу
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
            if(ch ==SYSTEM_SYMBOL[i]){
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

}
