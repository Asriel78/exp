package expression.exceptions;

import expression.*;
import java.util.List;

public class ExpressionParser implements ListParser {
    private String expression;
    private int pos;
    private List<String> variables;
    
    @Override
    public ListExpression parse(String expression, List<String> variables) throws ParsingException {
        this.expression = expression;
        this.pos = 0;
        this.variables = variables;
        
        try {
            ListExpression result = parseExpression();
            
            skipWhitespace();
            if (pos < expression.length()) {
                throw new ParsingException("Unexpected character: '" + expression.charAt(pos) + "' at position " + pos);
            }
            
            return result;
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException("Parsing error at position " + pos + ": " + e.getMessage(), e);
        }
    }
    
    private ListExpression parseExpression() throws ParsingException {
        return parseMinMax();
    }
    
    private ListExpression parseMinMax() throws ParsingException {
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
    
    private ListExpression parseAddSubtract() throws ParsingException {
        ListExpression result = parseMultiplyDivide();
        
        while (true) {
            skipWhitespace();
            if (pos >= expression.length()) break;
            
            char ch = expression.charAt(pos);
            
            if (ch == '+') {
                pos++;
                result = new CheckedAdd((TripleExpression) result, (TripleExpression) parseMultiplyDivide());
            } else if (ch == '-') {
                pos++;
                result = new CheckedSubtract((TripleExpression) result, (TripleExpression) parseMultiplyDivide());
            } else {
                break;
            }
        }
        
        return result;
    }
    
    private ListExpression parseMultiplyDivide() throws ParsingException {
        ListExpression result = parseUnary();
        
        while (true) {
            skipWhitespace();
            if (pos >= expression.length()) break;
            
            char ch = expression.charAt(pos);
            
            if (ch == '*') {
                pos++;
                result = new CheckedMultiply((TripleExpression) result, (TripleExpression) parseUnary());
            } else if (ch == '/') {
                pos++;
                result = new CheckedDivide((TripleExpression) result, (TripleExpression) parseUnary());
            } else {
                break;
            }
        }
        
        return result;
    }
    
    private ListExpression parseUnary() throws ParsingException {
        skipWhitespace();
        
        if (pos >= expression.length()) {
            throw new ParsingException("Unexpected end of expression");
        }
        
        if (expression.charAt(pos) == '-') {
            int minusPos = pos;
            pos++;
            
            boolean hasSpace = (pos < expression.length() && Character.isWhitespace(expression.charAt(pos)));
            skipWhitespace();
            
            if (!hasSpace && pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                return parseNumber(true);
            }
            
            return new CheckedNegate((TripleExpression) parseUnary());
        }
        
        if (checkWord("reverse")) {
            return new Reverse((TripleExpression) parseUnary());
        }
        
        return parsePrimary();
    }
    
    private ListExpression parsePrimary() throws ParsingException {
        skipWhitespace();
        
        if (pos >= expression.length()) {
            throw new ParsingException("Unexpected end of expression");
        }
        
        char ch = expression.charAt(pos);
        
        
        if (ch == '(') {
            pos++;
            ListExpression result = parseExpression();
            skipWhitespace();
            if (pos >= expression.length() || expression.charAt(pos) != ')') {
                throw new ParsingException("Missing closing parenthesis");
            }
            pos++;
            return result;
        }
        
        if (ch == '$') {
            pos++;
            if (pos >= expression.length() || !Character.isDigit(expression.charAt(pos))) {
                throw new ParsingException("Expected variable index after $");
            }
            
            int start = pos;
            while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                pos++;
            }
            
            int varIndex = Integer.parseInt(expression.substring(start, pos));
            if (varIndex < 0 || varIndex >= variables.size()) {
                throw new ParsingException("Variable index out of bounds: $" + varIndex);
            }
            return new Variable(varIndex);
        }
        
        
        if (Character.isDigit(ch)) {
            return parseNumber(false);
        }
        
        throw new ParsingException("Unexpected character: '" + ch + "' at position " + pos);
    }
    
    private ListExpression parseNumber(boolean negative) throws ParsingException {
        int start = pos;
        while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
            pos++;
        }
        
        String numStr = expression.substring(start, pos);
        
        try {
            if (negative && numStr.equals("2147483648")) {
                return new Const(Integer.MIN_VALUE);
            }
            
            int value = Integer.parseInt(numStr);
            if (negative) {
                if (value < 0) {
                    throw new ParsingException("Number overflow: -" + numStr);
                }
                return new Const(-value);
            }
            return new Const(value);
        } catch (NumberFormatException e) {
            throw new ParsingException("Number too large: " + (negative ? "-" : "") + numStr);
        }
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
}