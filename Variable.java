package expression;

import java.math.BigInteger;
import java.util.List;

public class Variable implements TripleExpression {
    private final Object identifier; // может быть String или Integer
    
    public Variable(String name) {
        this.identifier = name;
    }
    
    public Variable(int index) {
        this.identifier = index;
    }
    
    @Override
    public int evaluate(int x) {
        return x;
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
        return identifier.hashCode();
    }
}