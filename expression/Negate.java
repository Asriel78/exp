package expression;

import java.math.BigInteger;
import java.util.List;

public class Negate implements TripleExpression, BigIntegerListExpression, Expression, ListExpression {
    private final TripleExpression operand;
    
    public Negate(TripleExpression operand) {
        this.operand = operand;
    }
    
    @Override
    public int evaluate(int x) {
        return -operand.evaluate(x, 0, 0);
    }
    
    @Override
    public int evaluate(int x, int y, int z) {
        return -operand.evaluate(x, y, z);
    }
    
    @Override
    public int evaluate(List<Integer> variables) {
        if (operand instanceof ListExpression) {
            return -((ListExpression) operand).evaluate(variables);
        }
        return -operand.evaluate(variables.get(0), 0, 0);
    }
    
    @Override
    public BigInteger evaluateBi(List<BigInteger> variables) {
        if (operand instanceof BigIntegerListExpression) {
            return ((BigIntegerListExpression) operand).evaluateBi(variables).negate();
        }
        throw new IllegalStateException("Operand doesn't support BigInteger evaluation");
    }
    
    @Override
    public String toString() {
        return "-(" + operand.toString() + ")";
    }
    
    @Override
    public String toMiniString() {
        if (operand instanceof BinaryOperation) {
            return "-(" + operand.toMiniString() + ")";
        }
        return "- " + operand.toMiniString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Negate other = (Negate) obj;
        return operand.equals(other.operand);
    }
    
    @Override
    public int hashCode() {
        return operand.hashCode() * 31 + getClass().hashCode();
    }
}