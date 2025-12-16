package expression;

import java.math.BigInteger;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Тест с одной переменной (x^2 - 2*x + 1) ===");
        if (args.length > 0) {
            try {
                int x = Integer.parseInt(args[0]);
                
                TripleExpression expr = new Subtract(
                    new Subtract(
                        new Multiply(
                            new Variable("x"),
                            new Variable("x")
                        ),
                        new Multiply(
                            new Const(2),
                            new Variable("x")
                        )
                    ),
                    new Const(1)
                );
                
                System.out.println("Выражение: " + expr.toMiniString());
                System.out.println("Полная форма: " + expr.toString());
                System.out.println("При x = " + x + ", результат = " + expr.evaluate(x));
                
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: аргумент должен быть целым числом");
            }
        } else {
            // Значение по умолчанию для проверки формулы из задания
            int x = 10;
            TripleExpression expr = new Subtract(
                new Subtract(
                    new Multiply(
                        new Variable("x"),
                        new Variable("x")
                    ),
                    new Multiply(
                        new Const(2),
                        new Variable("x")
                    )
                ),
                new Const(1)
            );
            System.out.println("Выражение: " + expr.toMiniString());
            System.out.println("При x = " + x + ", результат = " + expr.evaluate(x));
            System.out.println("Ожидается: " + (x*x - 2*x + 1));
        }
        
        System.out.println("\n=== Тест с тремя переменными ===");
        TripleExpression expr2 = new Add(
            new Multiply(new Variable("x"), new Variable("y")),
            new Variable("z")
        );
        
        System.out.println("Выражение: " + expr2.toMiniString());
        System.out.println("Полная форма: " + expr2.toString());
        System.out.println("При x=2, y=3, z=4: " + expr2.evaluate(2, 3, 4));
        System.out.println("Ожидается: " + (2*3 + 4));
        
        System.out.println("\n=== Тест с BigInteger (позиционные переменные) ===");
        TripleExpression expr3 = new Subtract(
            new Add(
                new Variable(0),
                new Variable(1)
            ),
            new Const(BigInteger.ONE)
        );
        
        System.out.println("Выражение: " + expr3.toMiniString());
        System.out.println("Полная форма: " + expr3.toString());
        
        BigInteger result = expr3.evaluateBi(
            List.of(BigInteger.TWO, BigInteger.valueOf(3))
        );
        System.out.println("При $0 = 2, $1 = 3, результат = " + result);
        System.out.println("Ожидается: 4");
        
        System.out.println("\n=== Тест с большими числами ===");
        TripleExpression expr4 = new Multiply(
            new Add(
                new Variable(0),
                new Variable(1)
            ),
            new Const(new BigInteger("999999999999999999999999"))
        );
        
        System.out.println("Выражение: " + expr4.toMiniString());
        BigInteger bigResult = expr4.evaluateBi(
            List.of(
                new BigInteger("123456789012345678901234"),
                new BigInteger("987654321098765432109876")
            )
        );
        System.out.println("Результат: " + bigResult);
    }
}