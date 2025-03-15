import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Calculator {

    private static List<String> history = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Привет! Это калькулятор.");
        boolean working = true;

        while (working) {
            System.out.print("Введите выражение: ");
            String input = scanner.nextLine();

            try {
                double result = calculate(input);
                System.out.println("Результат: " + result);
                history.add(input + " = " + result); // Запись в историю
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            System.out.print("Продолжить? (y/n/history): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("n")) {
                working = false;
            } else if (choice.equalsIgnoreCase("history")) {
                showHistory();
            }
        }

        System.out.println("Спасибо за использование калькулятора!");
        scanner.close();
    }

    private static double calculate(String expression) throws Exception {
        expression = expression.replaceAll(" ", ""); // Убираем пробелы


        if (expression.contains("(")) {
            int start = expression.lastIndexOf("(");
            int end = expression.indexOf(")", start);
            if (end == -1) {
                throw new Exception("Ошибка: скобки не закрыты.");
            }
            String inside = expression.substring(start + 1, end);
            double resultInside = calculate(inside);
            expression = expression.substring(0, start) + resultInside + expression.substring(end + 1);
            return calculate(expression);
        }


        if (expression.startsWith("power(")) {
            return power(expression);
        } else if (expression.startsWith("sqrt(")) {
            return sqrt(expression);
        } else if (expression.startsWith("abs(")) {
            return abs(expression);
        } else if (expression.startsWith("round(")) {
            return round(expression);
        }


        return doMath(expression);
    }

    private static double power(String expression) throws Exception {
        int start = expression.indexOf("(");
        int end = expression.indexOf(")");
        String args = expression.substring(start + 1, end);
        String[] parts = args.split(",");
        if (parts.length != 2) {
            throw new Exception("Ошибка: нужно два числа для power.");
        }
        double a = Double.parseDouble(parts[0]);
        double b = Double.parseDouble(parts[1]);
        return Math.pow(a, b);
    }

    private static double sqrt(String expression) throws Exception {
        int start = expression.indexOf("(");
        int end = expression.indexOf(")");
        String arg = expression.substring(start + 1, end);
        double num = Double.parseDouble(arg);
        if (num < 0) {
            throw new Exception("Ошибка: корень из отрицательного числа.");
        }
        return Math.sqrt(num);
    }

    private static double abs(String expression) throws Exception {
        int start = expression.indexOf("(");
        int end = expression.indexOf(")");
        String arg = expression.substring(start + 1, end);
        double num = Double.parseDouble(arg);
        return Math.abs(num);
    }

    private static double round(String expression) throws Exception {
        int start = expression.indexOf("(");
        int end = expression.indexOf(")");
        String arg = expression.substring(start + 1, end);
        double num = Double.parseDouble(arg);
        return Math.round(num);
    }

    private static double doMath(String expression) throws Exception {

        String[] parts = expression.split("(?<=[*/%])|(?=[*/%])");
        if (parts.length > 1) {
            double result = Double.parseDouble(parts[0]);
            for (int i = 1; i < parts.length; i += 2) {
                String op = parts[i];
                double num = Double.parseDouble(parts[i + 1]);
                if (op.equals("*")) {
                    result *= num;
                } else if (op.equals("/")) {
                    if (num == 0) {
                        throw new Exception("Ошибка: деление на ноль.");
                    }
                    result /= num;
                } else if (op.equals("%")) {
                    result %= num;
                } else {
                    throw new Exception("Ошибка: неизвестный оператор " + op);
                }
            }
            return result;
        }



        parts = expression.split("(?<=[+-])|(?=[+-])");
        if (parts.length > 1) {
            double result = Double.parseDouble(parts[0]);
            for (int i = 1; i < parts.length; i += 2) {
                String op = parts[i];
                double num = Double.parseDouble(parts[i + 1]);
                if (op.equals("+")) {
                    result += num;
                } else if (op.equals("-")) {
                    result -= num;
                } else {
                    throw new Exception("Ошибка: неизвестный оператор " + op);
                }
            }
            return result;
        }


        return Double.parseDouble(expression);
    }

    private static void showHistory() {
        System.out.println("\nИстория вычислений:");
        for (String entry : history) {
            System.out.println(entry);
        }
        System.out.println();
    }
}