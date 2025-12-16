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
    
    // Уровень 1: +, - (самый низкий приоритет)
    private ListExpression parseExpression() {
        return parseAddSubtract();
    }
    
    // Уровень 2: +, -
    private ListExpression parseAddSubtract() {
        ListExpression result = parseMultiplyDivide();
        
        while (true) {
            skipWhitespace();
            if (pos >= expression.length()) break;
            
            char ch = expression.charAt(pos);
            
            if (ch == '+') {
                pos++;
                result = new Add((TripleExpression) result, (TripleExpression) parseMultiplyDivide());
            } else if (ch == '-') {
                pos++;
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
            
            char ch = expression.charAt(pos);
            
            if (ch == '*') {
                pos++;
                result = new Multiply((TripleExpression) result, (TripleExpression) parseUnary());
            } else if (ch == '/') {
                pos++;
                result = new Divide((TripleExpression) result, (TripleExpression) parseUnary());
            } else {
                break;
            }
        }
        
        return result;
    }
    
    // Уровень 4: унарные операции (-)
    private ListExpression parseUnary() {
        skipWhitespace();
        
        if (pos >= expression.length()) {
            throw error("Unexpected end of expression");
        }
        
        // Унарный минус
        if (expression.charAt(pos) == '-') {
            int minusPos = pos;
            pos++;
            
            // Проверяем, есть ли пробел после минуса
            // Если пробела НЕТ и сразу идёт цифра, то это отрицательное число
            boolean hasSpace = (pos < expression.length() && Character.isWhitespace(expression.charAt(pos)));
            skipWhitespace();
            
            // Парсим отрицательное число только если НЕТ пробела после минуса
            if (!hasSpace && pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                int start = pos;
                while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                    pos++;
                }
                String numStr = expression.substring(start, pos);
                try {
                    int value = Integer.parseInt(numStr);
                    return new Const(-value);
                } catch (NumberFormatException e) {
                    BigInteger value = new BigInteger(numStr);
                    return new Const(value.negate());
                }
            }
            
            // Иначе это унарный минус для выражения
            return new Negate((TripleExpression) parseUnary());
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
            if (pos >= expression.length() || !Character.isDigit(expression.charAt(pos))) {
                throw error("Expected variable index after $");
            }
            
            int start = pos;
            while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                pos++;
            }
            
            int varIndex = Integer.parseInt(expression.substring(start, pos));
            if (varIndex < 0 || varIndex >= variables.size()) {
                throw error("Variable index out of bounds: $" + varIndex);
            }
            return new Variable(varIndex);
        }
        
        // Число
        if (Character.isDigit(ch)) {
            int start = pos;
            while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                pos++;
            }
            
            String numStr = expression.substring(start, pos);
            try {
                int value = Integer.parseInt(numStr);
                return new Const(value);
            } catch (NumberFormatException e) {
                BigInteger value = new BigInteger(numStr);
                return new Const(value);
            }
        }
        
        throw error("Unexpected character: '" + ch + "'");
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