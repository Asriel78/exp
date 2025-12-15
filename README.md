Разработайте классы Const, Variable, Add, Subtract, Multiply, Divide для вычисления выражений с одной переменной в типе int (интерфейс Expression).
Классы должны позволять составлять выражения вида
new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
).evaluate(5)
            
При вычислении такого выражения вместо каждой переменной подставляется значение, переданное в качестве аргумента методу evaluate. Таким образом, результатом вычисления приведенного примера должно стать число 7.
Метод toString должен выдавать запись выражения в полноскобочной форме. Например
new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
).toString()
            
должен выдавать ((2 * x) - 3).
Сложный вариант. Метод toMiniString (интерфейс ToMiniString) должен выдавать выражение с минимальным числом скобок. Например
new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
).toMiniString()
            
должен выдавать 2 * x - 3.
Реализуйте метод equals, проверяющий, что два выражения совпадают. Например,
new Multiply(new Const(2), new Variable("x"))
    .equals(new Multiply(new Const(2), new Variable("x")))
            
должно выдавать true, а
new Multiply(new Const(2), new Variable("x"))
    .equals(new Multiply(new Variable("x"), new Const(2)))
            
должно выдавать false.
Для тестирования программы должен быть создан класс Main, который вычисляет значение выражения x2−2x+1, для x, заданного в командной строке.
При выполнении задания следует обратить внимание на:
Выделение общего интерфейса создаваемых классов.
Выделение абстрактного базового класса для бинарных операций.


Реализуйте интерфейс Expression
Исходный код тестов
Первый аргумент: easy или hard
Последующие аргументы: модификации

Дополнительно реализуйте поддержку вычисления выражений в типе BigInteger с позиционными переменными.
Конструктор позиционной переменной получает индекс переменной.
При выводе позиционная переменная должна иметь вид $index.
Метод вычисления выражения должен называться evaluateBi, ему передаётся список значений переменных.
Например, для expr = new Subtract(new Add(new Variable(0), new Variable(1)), new Const(BigInteger.ONE)):
expr.evaluateBi(List.of(BigInteger.TWO, BigInteger.THREE)) должно быть равно 4;
expr.toString() должно быть равно (($0 + $1) - 1).
