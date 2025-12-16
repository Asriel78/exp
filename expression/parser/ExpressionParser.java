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
    
    private ListExpression parseExpression() {
        return parseMinMax();
    }
    
    private ListExpression parseMinMax() {
        ListExpression result = parseAddSubtract();
        
        while (true) {
            skipWhitespace();
            if (pos >= expression.length()) break;
            
            if (checkWord("min")) {
                result = new Min((TripleExpression) result, (TripleExpression) parseAddSubtract());
            } else if (checkWord("max")) {
                result = new Max((TripleExpression) result, (TripleExpression) parseAddSubtract());
            } else {
                break;
            }
        }
        
        return result;
    }
    
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
    
    private ListExpression parseUnary() {
        skipWhitespace();
        
        if (pos >= expression.length()) {
            throw error("Unexpected end of expression");
        }
        
        if (expression.charAt(pos) == '-') {
            int minusPos = pos;
            pos++;
            
            boolean hasSpace = (pos < expression.length() && Character.isWhitespace(expression.charAt(pos)));
            skipWhitespace();
            
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
            
            return new Negate((TripleExpression) parseUnary());
        }
        
        if (checkWord("reverse")) {
            return new Reverse((TripleExpression) parseUnary());
        }
        
        return parsePrimary();
    }
    
    private ListExpression parsePrimary() {
        skipWhitespace();
        
        if (pos >= expression.length()) {
            throw error("Unexpected end of expression");
        }
        
        char ch = expression.charAt(pos);
        
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
    
    private void skipWhitespace() {
        while (pos < expression.length() && Character.isWhitespace(expression.charAt(pos))) {
            pos++;
        }
    }
    
    private boolean checkWord(String word) {
        skipWhitespace();
        
        if (pos + word.length() > expression.length()) {
            return false;
        }
        
        if (!expression.substring(pos, pos + word.length()).equals(word)) {
            return false;
        }
        
        if (pos + word.length() < expression.length()) {
            char next = expression.charAt(pos + word.length());
            if (Character.isLetterOrDigit(next)) {
                return false;
            }
        }
        
        pos += word.length();
        return true;
    }
    
    private IllegalArgumentException error(String message) {
        return new IllegalArgumentException(message + " at position " + pos);
    }
}