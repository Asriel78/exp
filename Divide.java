package expression;

import java.math.BigInteger;

public class Divide extends BinaryOperation {
    public Divide(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        return a / b;
    }
    
    @Override
    protected BigInteger applyBi(BigInteger a, BigInteger b) {
        return a.divide(b);
    }
    
    @Override
    protected String getOperator() {
        return "/";
    }
    
    @Override
    protected int getPriority() {
        return 2;
    }
    
    @Override
    protected boolean isAssociative() {
        return false;
    }
}
