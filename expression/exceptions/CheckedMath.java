package expression.exceptions;

public class CheckedMath {
    
    public static int add(int a, int b) {
        if (b > 0 && a > Integer.MAX_VALUE - b) {
            throw new OverflowException("overflow: " + a + " + " + b);
        }
        if (b < 0 && a < Integer.MIN_VALUE - b) {
            throw new OverflowException("overflow: " + a + " + " + b);
        }
        return a + b;
    }
    
    public static int subtract(int a, int b) {
        if (b > 0 && a < Integer.MIN_VALUE + b) {
            throw new OverflowException("overflow: " + a + " - " + b);
        }
        if (b < 0 && a > Integer.MAX_VALUE + b) {
            throw new OverflowException("overflow: " + a + " - " + b);
        }
        return a - b;
    }
    
    public static int multiply(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        
        if (a == -1) {
            if (b == Integer.MIN_VALUE) {
                throw new OverflowException("overflow: " + a + " * " + b);
            }
            return -b;
        }
        
        if (b == -1) {
            if (a == Integer.MIN_VALUE) {
                throw new OverflowException("overflow: " + a + " * " + b);
            }
            return -a;
        }
        
        // Проверка переполнения
        if (a > 0 && b > 0) {
            if (a > Integer.MAX_VALUE / b) {
                throw new OverflowException("overflow: " + a + " * " + b);
            }
        } else if (a > 0 && b < 0) {
            if (b < Integer.MIN_VALUE / a) {
                throw new OverflowException("overflow: " + a + " * " + b);
            }
        } else if (a < 0 && b > 0) {
            if (a < Integer.MIN_VALUE / b) {
                throw new OverflowException("overflow: " + a + " * " + b);
            }
        } else { // a < 0 && b < 0
            if (a < Integer.MAX_VALUE / b) {
                throw new OverflowException("overflow: " + a + " * " + b);
            }
        }
        
        return a * b;
    }
    
    public static int divide(int a, int b) {
        if (b == 0) {
            throw new DivisionByZeroException("division by zero: " + a + " / " + b);
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException("overflow: " + a + " / " + b);
        }
        return a / b;
    }
    
    public static int negate(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("overflow: -(" + a + ")");
        }
        return -a;
    }
}