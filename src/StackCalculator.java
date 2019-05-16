import java.util.Scanner;

abstract class Stack {
    int top = -1;
    int size = 100;

    boolean isEmpty() {
        return top == -1;
    }
}

class Operator extends Stack {
    private String[] stack = new String[size];

    void push(String op) {
        stack[++top] = op;
    }

    String pop() {
        return stack[top--];
    }
}

class Operand extends Stack {
    private double[] stack = new double[size];

    void push(double num) {
        stack[++top] = num;
    }

    double pop() {
        return stack[top--];
    }
}

public class StackCalculator {
    public static void main(String[] args) throws Exception {
        System.out.println("====================== Stack Calculator ======================");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(" >> ");
            String expression = scanner.nextLine();
            if (expression.equals("exit")) System.exit(-1);
            if (syntaxCondition(expression)) {
                String post_expression = transPostfix(expression);
                double result = calculate(post_expression);
                System.out.println("Postfix : " + post_expression);
                System.out.println("Result : " + result);
            }
        }
    }

    private static String transPostfix(String expression) {
        String[] expressionArr = expression.split(" ");
        StringBuilder post_expression = new StringBuilder();
        Operator operator = new Operator();
        for (String exp : expressionArr) {
            switch (exp) {
                case "(":
                case "{":
                case "[":
                    operator.push(exp);
                    break;
                case ")":
                case "}":
                case "]":
                    while (true) {
                        String op = operator.pop();
                        if (!op.equals("(") && !op.equals("{") && !op.equals("[")) {
                            post_expression.append(op).append(" ");
                        } else {
                            break;
                        }
                    }
                    break;
                case "+":
                case "-":
                    while (true) {
                        if (operator.isEmpty()) {
                            break;
                        }
                        String op = operator.pop();
                        if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")) {
                            post_expression.append(op).append(" ");
                        } else {
                            operator.push(op);
                            break;
                        }
                    }
                    operator.push(exp);
                    break;
                case "*":
                case "/":
                    while (true) {
                        if (operator.isEmpty()) {
                            break;
                        }
                        String op = operator.pop();
                        if (op.equals("*") || op.equals("/")) {
                            post_expression.append(op).append(" ");
                        } else {
                            operator.push(op);
                            break;
                        }
                    }
                    operator.push(exp);
                    break;
                default:
                    post_expression.append(exp).append(" ");
                    break;
            }
        }
        while (!operator.isEmpty()) {
            post_expression.append(operator.pop()).append(" ");
        }
        return post_expression.toString();
    }

    private static Double calculate(String post_expression) {
        Operand operand = new Operand();
        String[] expressionArr = post_expression.split(" ");
        for (String exp : expressionArr) {
            try {
                double number = Double.parseDouble(exp);
                operand.push(number);
            } catch (NumberFormatException e) {
                double op1 = operand.pop();
                double op2 = operand.pop();
                switch (exp) {
                    case "+":
                        operand.push(op2 + op1);
                        break;
                    case "-":
                        operand.push(op2 - op1);
                        break;
                    case "*":
                        operand.push(op2 * op1);
                        break;
                    case "/":
                        operand.push(op2 / op1);
                        break;
                }
            }
        }
        return operand.pop();
    }

    private static boolean syntaxCondition(String expression) throws Exception {
        String[] expressionArr = expression.split(" ");
        Operator operator = new Operator();
        int number_count = 0;
        for (int i = 0; i < expressionArr.length; i++) {
            switch (expressionArr[i]) {
                case "+":
                case "-":
                case "*":
                case "/":
                    if (expressionArr[i + 1].equals("+") || expressionArr[i + 1].equals("-")
                            || expressionArr[i + 1].equals("*") || expressionArr[i + 1].equals("/")) {
                        throw new Exception("syntaxCondition(): Operator error");
                    }
                    break;
                case "(":
                case "{":
                case "[":
                    operator.push(expressionArr[i]);
                    break;
                case ")":
                    if (operator.isEmpty()) {
                        throw new Exception("syntaxCondition(): Syntax error");
                    } else {
                        if (!operator.pop().equals("(")) {
                            throw new Exception("syntaxCondition(): Syntax error");
                        }
                    }
                    break;
                case "}":
                    if (operator.isEmpty()) {
                        throw new Exception("syntaxCondition(): Syntax error");
                    } else {
                        if (!operator.pop().equals("}")) {
                            throw new Exception("syntaxCondition(): Syntax error");
                        }
                    }
                    break;
                case "]":
                    if (operator.isEmpty()) {
                        throw new Exception("syntaxCondition(): Syntax error");
                    } else {
                        if (!operator.pop().equals("]")) {
                            throw new Exception("syntaxCondition(): Syntax error");
                        }
                    }
                    break;
                default:
                    try {
                        Double.parseDouble(expressionArr[i]);
                        number_count++;
                    } catch (NumberFormatException e) {
                        throw new Exception("syntaxCondition(): There is not number in formular");
                    }
                    break;
            }
        }
        if (number_count == 0 || number_count == 1) {
            throw new Exception("syntaxCondition(): There is no number or unavailable in formular");
        }
        while (!operator.isEmpty()) {
            String op = operator.pop();
            if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")
                    || op.equals("(") || op.equals("{") || op.equals("[")) {
                throw new Exception("syntaxCondition(): '" + op + "'Syntax error");
            }
        }
        return true;
    }
}
