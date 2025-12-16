package expression.parser;

import expression.*;
import java.math.BigInteger;
import java.util.List;

public class ExpressionParser implements ListParser {
    private String expression;
    private int pos;
    private List<String> variables;
    
    @Override
    public ListExpression parse(String expression, List<String> variables) {
        this.expression = expression;
        this.pos = 0;
        this.variables = variables;
        
        ListExpression result = parseExpression();
        
        skipWhitespace();
        if (pos < expression.length()) {
            throw error("Unexpected character: '" + expression.charAt(pos) + "'");
        }
        
        return result;
    }
    
    // Уровень 1: min, max (самый низкий приоритет)
    private ListExpression parseExpression() {
        return parseAddSubtract();
    }
    
    // Уровень 2: +, -
    private ListExpression parseAddSubtract() {
        ListExpression result = parseMultiplyDivide();
        
        while (true) {
            skipWhitespace();
            if (pos >= expression.length()) break;
            
            if (checkWord("+")) {
                result = new Add((TripleExpression) result, (TripleExpression) parseMultiplyDivide());
            } else if (checkWord("-")) {
                result = new Subtract((TripleExpression) result, (TripleExpression) parseMultiplyDivide());
            } else {
                break;
            }
        }
        
        return result;
    }
    
    // Уровень 3: *, /
    private ListExpression parseMultiplyDivide() {
        ListExpression result = parseUnary();
        
        while (true) {
            skipWhitespace();
            if (pos >= expression.length()) break;
            
            if (checkWord("*")) {
                result = new Multiply((TripleExpression) result, (TripleExpression) parseUnary());
            } else if (checkWord("/")) {
                result = new Divide((TripleExpression) result, (TripleExpression) parseUnary());
            } else {
                break;
            }
        }
        
        return result;
    }
    
    // Уровень 4: унарные операции (-, reverse и т.д.)
    private ListExpression parseUnary() {
        skipWhitespace();
        
        if (pos >= expression.length()) {
            throw error("Unexpected end of expression");
        }
        
        // Унарный минус
        if (checkWord("-")) {
            return new Subtract((TripleExpression) new Const(0), (TripleExpression) parseUnary());
        }
        
        return parsePrimary();
    }
    
    // Уровень 5: переменные, константы, скобки
    private ListExpression parsePrimary() {
        skipWhitespace();
        
        if (pos >= expression.length()) {
            throw error("Unexpected end of expression");
        }
        
        char ch = expression.charAt(pos);
        
        // Скобки
        if (ch == '(') {
            pos++;
            ListExpression result = parseExpression();
            skipWhitespace();
            if (pos >= expression.length() || expression.charAt(pos) != ')') {
                throw error("Missing closing parenthesis");
            }
            pos++;
            return result;
        }
        
        // Переменная $0, $1, $2, ...
        if (ch == '$') {
            pos++;
            int varIndex = parseInteger();
            if (varIndex < 0 || varIndex >= variables.size()) {
                throw error("Variable index out of bounds: $" + varIndex);
            }
            return new Variable(varIndex);
        }
        
        // Число
        if (Character.isDigit(ch)) {
            return parseNumber();
        }
        
        throw error("Unexpected character: '" + ch + "'");
    }
    
    // Парсинг целого числа (константа)
    private ListExpression parseNumber() {
        skipWhitespace();
        int start = pos;
        
        while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
            pos++;
        }
        
        String numStr = expression.substring(start, pos);
        
        try {
            int value = Integer.parseInt(numStr);
            return new Const(value);
        } catch (NumberFormatException e) {
            // Если не влезает в int, используем BigInteger
            BigInteger value = new BigInteger(numStr);
            return new Const(value);
        }
    }
    
    // Парсинг целого числа (для индекса переменной)
    private int parseInteger() {
        skipWhitespace();
        int start = pos;
        
        while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
            pos++;
        }
        
        if (start == pos) {
            throw error("Expected number");
        }
        
        return Integer.parseInt(expression.substring(start, pos));
    }
    
    // Проверка и парсинг слова/оператора
    private boolean checkWord(String word) {
        skipWhitespace();
        
        if (pos + word.length() > expression.length()) {
            return false;
        }
        
        if (!expression.substring(pos, pos + word.length()).equals(word)) {
            return false;
        }
        
        // Проверяем, что после слова нет буквы/цифры (для операций типа "min", "max")
        if (word.length() > 1 && pos + word.length() < expression.length()) {
            char next = expression.charAt(pos + word.length());
            if (Character.isLetterOrDigit(next)) {
                return false;
            }
        }
        
        pos += word.length();
        return true;
    }
    
    // Пропуск пробелов
    private void skipWhitespace() {
        while (pos < expression.length() && Character.isWhitespace(expression.charAt(pos))) {
            pos++;
        }
    }
    
    // Создание исключения с информацией о позиции
    private IllegalArgumentException error(String message) {
        return new IllegalArgumentException(message + " at position " + pos);
    }
}