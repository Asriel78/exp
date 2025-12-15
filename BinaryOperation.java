package expression;

import java.math.BigInteger;
import java.util.List;

public abstract class BinaryOperation implements TripleExpression {
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
        return apply(left.evaluate(x), right.evaluate(x));
    }
    
    @Override
    public BigInteger evaluateBi(List<BigInteger> variables) {
        return applyBi(left.evaluateBi(variables), right.evaluateBi(variables));
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
            return !isAssociative();
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
        return 31 * left.hashCode() + right.hashCode();
    }
}