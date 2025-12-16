package expression;

import java.math.BigInteger;

public class Power extends BinaryOperation {
    public Power(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        if (b < 0) {
            throw new IllegalArgumentException("Negative exponent: " + b);
        }
        if (b == 0) {
            return 1;
        }
        if (a == 0) {
            return 0;
        }
        
        int result = 1;
        int base = a;
        int exp = b;
        
        while (exp > 0) {
            if (exp % 2 == 1) {
                result *= base;
            }
            base *= base;
            exp /= 2;
        }
        
        return result;
    }
    
    @Override
    protected BigInteger applyBi(BigInteger a, BigInteger b) {
        if (b.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative exponent: " + b);
        }
        if (b.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new IllegalArgumentException("Exponent too large: " + b);
        }
        return a.pow(b.intValue());
    }
    
    @Override
    protected String getOperator() {
        return "**";
    }
    
    @Override
    protected int getPriority() {
        return 3;
    }
    
    @Override
    protected boolean isAssociative() {
        return false;
    }
}