package expression;

import java.math.BigInteger;
import java.util.List;

public class Reverse implements TripleExpression, BigIntegerListExpression, Expression, ListExpression {
    private final TripleExpression operand;
    
    public Reverse(TripleExpression operand) {
        this.operand = operand;
    }
    
    private int reverseInt(int value) {
        int result = 0;
        while (value != 0) {
            int digit = value % 10;
            value /= 10;
            result = result * 10 + digit;
        }
        return result;
    }
    
    private BigInteger reverseBigInteger(BigInteger value) {
        boolean negative = value.signum() < 0;
        if (negative) {
            value = value.negate();
        }
        
        String str = value.toString();
        String reversed = new StringBuilder(str).reverse().toString();
        BigInteger result = new BigInteger(reversed);
        
        return negative ? result.negate() : result;
    }
    
    @Override
    public int evaluate(int x) {
        return reverseInt(operand.evaluate(x, 0, 0));
    }
    
    @Override
    public int evaluate(int x, int y, int z) {
        return reverseInt(operand.evaluate(x, y, z));
    }
    
    @Override
    public int evaluate(List<Integer> variables) {
        if (operand instanceof ListExpression) {
            return reverseInt(((ListExpression) operand).evaluate(variables));
        }
        return reverseInt(operand.evaluate(variables.get(0), 0, 0));
    }
    
    @Override
    public BigInteger evaluateBi(List<BigInteger> variables) {
        if (operand instanceof BigIntegerListExpression) {
            return reverseBigInteger(((BigIntegerListExpression) operand).evaluateBi(variables));
        }
        throw new IllegalStateException("Operand doesn't support BigInteger evaluation");
    }
    
    @Override
    public String toString() {
        return "reverse(" + operand.toString() + ")";
    }
    
    @Override
    public String toMiniString() {
        if (operand instanceof BinaryOperation) {
            return "reverse(" + operand.toMiniString() + ")";
        }
        return "reverse " + operand.toMiniString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reverse other = (Reverse) obj;
        return operand.equals(other.operand);
    }
    
    @Override
    public int hashCode() {
        return operand.hashCode() * 31 + getClass().hashCode();
    }
}