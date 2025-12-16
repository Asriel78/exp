package expression.exceptions;

import expression.*;

public class CheckedAdd extends Add {
    public CheckedAdd(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        return CheckedMath.add(a, b);
    }
}