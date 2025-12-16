package expression.exceptions;

import expression.*;
import java.util.List;

public class CheckedNegate extends Negate {
    private final TripleExpression operand;
    
    public CheckedNegate(TripleExpression operand) {
        super(operand);
        this.operand = operand;
    }
    
    @Override
    public int evaluate(int x, int y, int z) {
        int value = operand.evaluate(x, y, z);
        return CheckedMath.negate(value);
    }
    
    @Override
    public int evaluate(int x) {
        if (operand instanceof Expression) {
            return CheckedMath.negate(((Expression) operand).evaluate(x));
        }
        return CheckedMath.negate(operand.evaluate(x, 0, 0));
    }
    
    @Override
    public int evaluate(List<Integer> variables) {
        if (operand instanceof ListExpression) {
            return CheckedMath.negate(((ListExpression) operand).evaluate(variables));
        }
        return CheckedMath.negate(operand.evaluate(variables.get(0), 0, 0));
    }
}