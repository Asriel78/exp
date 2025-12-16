package expression;

import java.math.BigInteger;
import java.util.List;

public class Variable implements TripleExpression, BigIntegerListExpression, Expression, ListExpression {
    private final Object identifier;
    
    public Variable(String name) {
        this.identifier = name;
    }
    
    public Variable(int index) {
        this.identifier = index;
    }
    
    @Override
    public int evaluate(int x) {
        if (identifier instanceof String) {
            String name = (String) identifier;
            if ("x".equals(name)) return x;
        }
        if (identifier instanceof Integer) {
            int index = (Integer) identifier;
            if (index == 0) return x;
        }
        throw new IllegalArgumentException("Invalid variable for single argument");
    }
    
    @Override
    public int evaluate(int x, int y, int z) {
        if (identifier instanceof String) {
            String name = (String) identifier;
            switch (name) {
                case "x": return x;
                case "y": return y;
                case "z": return z;
                default: throw new IllegalArgumentException("Unknown variable: " + name);
            }
        }
        int index = (Integer) identifier;
        switch (index) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: throw new IllegalArgumentException("Invalid variable index: " + index);
        }
    }
    
    @Override
    public int evaluate(List<Integer> variables) {
        if (identifier instanceof Integer) {
            int index = (Integer) identifier;
            return variables.get(index);
        }
        throw new IllegalStateException("Named variables not supported for List<Integer> evaluation");
    }
    
    @Override
    public BigInteger evaluateBi(List<BigInteger> variables) {
        if (identifier instanceof Integer) {
            int index = (Integer) identifier;
            return variables.get(index);
        }
        throw new IllegalStateException("Named variables not supported for BigInteger evaluation");
    }
    
    @Override
    public String toString() {
        if (identifier instanceof Integer) {
            return "$" + identifier;
        }
        return (String) identifier;
    }
    
    @Override
    public String toMiniString() {
        return toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Variable other = (Variable) obj;
        return identifier.equals(other.identifier);
    }
    
    @Override
    public int hashCode() {
        return identifier.hashCode() * 31 + getClass().hashCode();
    }
}