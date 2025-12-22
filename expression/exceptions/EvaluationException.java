package expression.exceptions;

// Базовое исключение для вычислений
public class EvaluationException extends RuntimeException {
    public EvaluationException(String message) {
        super(message);
    }
}