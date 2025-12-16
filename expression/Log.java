package expression;

import java.math.BigInteger;

public class Log extends BinaryOperation {
    public Log(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        if (a <= 0) {
            throw new IllegalArgumentException("Logarithm base must be positive: " + a);
        }
        if (b <= 1) {
            throw new IllegalArgumentException("Logarithm argument must be > 1: " + b);
        }
        
        int result = 0;
        int temp = a;
        
        while (temp >= b) {
            temp /= b;
            result++;
        }
        
        return result;
    }
    
    @Override
    protected BigInteger applyBi(BigInteger a, BigInteger b) {
        if (a.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("Logarithm base must be positive: " + a);
        }
        if (b.compareTo(BigInteger.ONE) <= 0) {
            throw new IllegalArgumentException("Logarithm argument must be > 1: " + b);
        }
        
        BigInteger result = BigInteger.ZERO;
        BigInteger temp = a;
        
        while (temp.compareTo(b) >= 0) {
            temp = temp.divide(b);
            result = result.add(BigInteger.ONE);
        }
        
        return result;
    }
    
    @Override
    protected String getOperator() {
        return "//";
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