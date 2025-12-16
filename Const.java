package expression;

import java.math.BigInteger;
import java.util.List;

public class Const implements TripleExpression, BigIntegerListExpression, Expression, ListExpression {
    private final Number value;
    
    public Const(int value) {
        this.value = value;
    }
    
    public Const(BigInteger value) {
        this.value = value;
    }
    
    // Для Expression (одна переменная)
    @Override
    public int evaluate(int x) {
        return value.intValue();
    }
    
    // Для TripleExpression (три переменные)
    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }
    
    // Для ListExpression (список Integer)
    @Override
    public int evaluate(List<Integer> variables) {
        return value.intValue();
    }
    
    // Для BigIntegerListExpression (список BigInteger)
    @Override
    public BigInteger evaluateBi(List<BigInteger> variables) {
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        return BigInteger.valueOf(value.intValue());
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
    
    @Override
    public String toMiniString() {
        return toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Const other = (Const) obj;
        return value.equals(other.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode() * 31 + getClass().hashCode();
    }
}