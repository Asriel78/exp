package expression.exceptions;

import expression.*;
import java.math.BigInteger;

class CheckedLog extends Log {
    public CheckedLog(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
    
    @Override
    protected int apply(int a, int b) {
        if (a <= 0) {
            throw new EvaluationException("Logarithm base must be positive: " + a);
        }
        if (b <= 1) {
            throw new EvaluationException("Logarithm argument must be > 1: " + b);
        }

        int result = 0;
        int temp = a;
        
        while (temp >= b) {
            temp /= b;
            result++;
        }
        
        return result;
    }
}