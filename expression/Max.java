package expression;

import java.math.BigInteger;

public class Max extends BinaryOperation {
    public Max(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        return Math.max(a, b);
    }
    
    @Override
    protected BigInteger applyBi(BigInteger a, BigInteger b) {
        return a.max(b);
    }
    
    @Override
    protected String getOperator() {
        return "max";
    }
    
    @Override
    protected int getPriority() {
        return 0; 
    }
    
    @Override
    protected boolean isAssociative() {
        return true;
    }
}