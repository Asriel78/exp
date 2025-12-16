package expression;

import java.math.BigInteger;
import java.util.List;

public abstract class BinaryOperation implements TripleExpression, BigIntegerListExpression, Expression, ListExpression {
    protected final TripleExpression left;
    protected final TripleExpression right;
    
    public BinaryOperation(TripleExpression left, TripleExpression right) {
        this.left = left;
        this.right = right;
    }
    
    protected abstract int apply(int a, int b);
    protected abstract BigInteger applyBi(BigInteger a, BigInteger b);
    protected abstract String getOperator();
    protected abstract int getPriority();
    protected abstract boolean isAssociative();
    
    @Override
    public int evaluate(int x) {
        return apply(
            left instanceof Expression ? ((Expression) left).evaluate(x) : left.evaluate(x, 0, 0),
            right instanceof Expression ? ((Expression) right).evaluate(x) : right.evaluate(x, 0, 0)
        );
    }
    
    @Override
    public int evaluate(int x, int y, int z) {
        return apply(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
    
    @Override
    public int evaluate(List<Integer> variables) {
        int leftVal = left instanceof ListExpression ? 
            ((ListExpression) left).evaluate(variables) : 
            left.evaluate(variables.get(0), 0, 0);
        int rightVal = right instanceof ListExpression ? 
            ((ListExpression) right).evaluate(variables) : 
            right.evaluate(variables.get(0), 0, 0);
        return apply(leftVal, rightVal);
    }
    
    @Override
    public BigInteger evaluateBi(List<BigInteger> variables) {
        BigInteger leftVal = left instanceof BigIntegerListExpression ? 
            ((BigIntegerListExpression) left).evaluateBi(variables) : null;
        BigInteger rightVal = right instanceof BigIntegerListExpression ? 
            ((BigIntegerListExpression) right).evaluateBi(variables) : null;
        return applyBi(leftVal, rightVal);
    }
    
    @Override
    public String toString() {
        return "(" + left.toString() + " " + getOperator() + " " + right.toString() + ")";
    }
    
    @Override
    public String toMiniString() {
        String leftStr = needsLeftParentheses() ? "(" + left.toMiniString() + ")" : left.toMiniString();
        String rightStr = needsRightParentheses() ? "(" + right.toMiniString() + ")" : right.toMiniString();
        return leftStr + " " + getOperator() + " " + rightStr;
    }
    
    private boolean needsLeftParentheses() {
        if (!(left instanceof BinaryOperation)) return false;
        BinaryOperation leftOp = (BinaryOperation) left;
        return leftOp.getPriority() < getPriority();
    }
    
    private boolean needsRightParentheses() {
        if (!(right instanceof BinaryOperation)) return false;
        BinaryOperation rightOp = (BinaryOperation) right;
        
        if (rightOp.getPriority() < getPriority()) {
            return true;
        }
        
        if (rightOp.getPriority() == getPriority()) {
            if (getPriority() == 0) { 
                if (getClass().equals(rightOp.getClass())) {
                    return false;
                }
                return true;
            }
            
            if (getClass().equals(rightOp.getClass())) {
                return !isAssociative();
            }
            
            if (!isAssociative()) {
                return true;
            }
            return getPriority() == 2;
        }
        
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BinaryOperation other = (BinaryOperation) obj;
        return left.equals(other.left) && right.equals(other.right);
    }
    
    @Override
    public int hashCode() {
        return (31 * left.hashCode() + right.hashCode()) * 31 + getClass().hashCode();
    }
}