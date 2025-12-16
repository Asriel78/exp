package expression.exceptions;

import expression.*;
import java.math.BigInteger;

class CheckedPower extends Power {
    public CheckedPower(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        if (b < 0) {
            throw new EvaluationException("Negative exponent: " + b);
        }
        if (b == 0) {
            return 1;
        }
        if (a == 0) {
            return 0;
        }
        if (a == 1) {
            return 1;
        }
        if (a == -1) {
            return b % 2 == 0 ? 1 : -1;
        }
        
        int result = 1;
        for (int i = 0; i < b; i++) {
            result = CheckedMath.multiply(result, a);
        }
        
        return result;
    }
}
