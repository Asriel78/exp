package expression;

import java.math.BigInteger;

public class Subtract extends BinaryOperation {
    public Subtract(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        return a - b;
    }
    
    @Override
    protected BigInteger applyBi(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }
    
    @Override
    protected String getOperator() {
        return "-";
    }
    
    @Override
    protected int getPriority() {
        return 1;
    }
    
    @Override
    protected boolean isAssociative() {
        return false;
    }
}
