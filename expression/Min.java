package expression;

import java.math.BigInteger;

public class Min extends BinaryOperation {
    public Min(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        return Math.min(a, b);
    }
    
    @Override
    protected BigInteger applyBi(BigInteger a, BigInteger b) {
        return a.min(b);
    }
    
    @Override
    protected String getOperator() {
        return "min";
    }
    
    @Override
    protected int getPriority() {
        return 0; // Самый низкий приоритет
    }
    
    @Override
    protected boolean isAssociative() {
        return true;
    }
}