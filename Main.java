package expression;

import java.math.BigInteger;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Тест с int ===");
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
        }
        
        System.out.println("\n=== Тест с BigInteger ===");
        TripleExpression expr2 = new Subtract(
            new Add(
                new Variable(0),
                new Variable(1)
            ),
            new Const(BigInteger.ONE)
        );
        
        System.out.println("Выражение: " + expr2.toMiniString());
        System.out.println("Полная форма: " + expr2.toString());
        
        BigInteger result = expr2.evaluateBi(
            List.of(BigInteger.TWO, BigInteger.valueOf(3))
        );
        System.out.println("При $0 = 2, $1 = 3, результат = " + result);
        

        System.out.println("\n=== Тест с большими числами ===");
        TripleExpression expr3 = new Multiply(
            new Add(
                new Variable(0),
                new Variable(1)
            ),
            new Const(new BigInteger("999999999999999999999999"))
        );
        
        System.out.println("Выражение: " + expr3.toMiniString());
        BigInteger bigResult = expr3.evaluateBi(
            List.of(
                new BigInteger("123456789012345678901234"),
                new BigInteger("987654321098765432109876")
            )
        );
        System.out.println("Результат: " + bigResult);
    }
}