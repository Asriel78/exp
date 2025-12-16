package expression.exceptions;

import expression.*;

public class CheckedDivide extends Divide {
    public CheckedDivide(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        return CheckedMath.divide(a, b);
    }
}