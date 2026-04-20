package zavd4;

import java.util.*;

/**
 * Базовий інтерфейс фабрикованих об'єктів
 */
interface ResultView {
    void display();

    // overloading (перевантаження)
    void display(String title);
}

/**
 * Відображення у вигляді таблиці
 */
class TableResultView implements ResultView {

    private Map<Integer, Integer> data;
    private int columnWidth;

    public TableResultView(Map<Integer, Integer> data, int columnWidth) {
        this.data = data;
        this.columnWidth = columnWidth;
    }

    // overriding (перевизначення)
    @Override
    public void display() {
        display("РЕЗУЛЬТАТИ ТАБЛИЦІ");
    }

    // overriding + overloading (перевантаження)
    @Override
    public void display(String title) {
        System.out.println("\n=== " + title + " ===");

        System.out.printf("%-" + columnWidth + "s | %-" + columnWidth + "s%n", "Цифра", "Кількість");
        System.out.println("-".repeat(columnWidth * 2 + 3));

        for (Map.Entry<Integer, Integer> e : data.entrySet()) {
            System.out.printf("%-" + columnWidth + "d | %-" + columnWidth + "d%n",
                    e.getKey(), e.getValue());
        }
    }
}

/**
 * Абстрактна фабрика
 */
interface ViewFactory {
    ResultView createView(Map<Integer, Integer> data);
}

/**
 * Фабрика таблиці (параметризована користувачем)
 */
class TableViewFactory implements ViewFactory {

    private int columnWidth;

    public TableViewFactory(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    @Override
    public ResultView createView(Map<Integer, Integer> data) {
        return new TableResultView(data, columnWidth);
    }
}

/**
 * Клас обчислення частоти цифр
 */
class DigitCalculator {

    public Map<Integer, Integer> calculate(int number) {
        Map<Integer, Integer> result = new LinkedHashMap<>();
        int n = Math.abs(number);

        if (n == 0) {
            result.put(0, 1);
            return result;
        }

        while (n > 0) {
            int d = n % 10;
            result.put(d, result.getOrDefault(d, 0) + 1);
            n /= 10;
        }

        return result;
    }
}

/**
 * Демонстрація функціональності (діалоговий режим)
 */
public class zavd4 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DigitCalculator calc = new DigitCalculator();

        // dynamic method dispatch (поліморфізм)
        ResultView view;

        System.out.print("Введіть число: ");
        int number = sc.nextInt();

        Map<Integer, Integer> result = calc.calculate(number);

        // користувацький параметр таблиці
        System.out.print("Введіть ширину стовпців таблиці: ");
        int width = sc.nextInt();

        // Factory Method
        ViewFactory factory = new TableViewFactory(width);
        view = factory.createView(result);

        // dynamic method dispatch
        view.display();
        view.display("КОРИСТУВАЦЬКА ТАБЛИЦЯ");

        sc.close();
    }
}