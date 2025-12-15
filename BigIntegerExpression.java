package expression;

import java.math.BigInteger;
import java.util.List;

public interface BigIntegerExpression extends ToMiniString {
    BigInteger evaluateBi(List<BigInteger> variables);
}
