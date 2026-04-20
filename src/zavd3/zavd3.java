package zavd3;
import java.io.*;
import java.util.*;

/**
 * Інтерфейс "фабрикованих" об'єктів (відображення результатів)
 */
interface ResultView extends Serializable {
    void display();
}

/**
 * Текстове відображення результатів
 */
class TextResultView implements ResultView {
    private static final long serialVersionUID = 1L;

    private Map<Integer, Integer> data;

    public TextResultView(Map<Integer, Integer> data) {
        this.data = data;
    }

    @Override
    public void display() {
        System.out.println("=== Текстовий результат ===");
        for (Map.Entry<Integer, Integer> e : data.entrySet()) {
            System.out.println("Цифра " + e.getKey() + " -> " + e.getValue());
        }
    }
}

/**
 * Фабрика (Virtual Constructor)
 */
interface ViewFactory {
    ResultView createView(Map<Integer, Integer> data);
}

/**
 * Конкретна фабрика для текстового відображення
 */
class TextViewFactory implements ViewFactory {
    @Override
    public ResultView createView(Map<Integer, Integer> data) {
        return new TextResultView(data);
    }
}

/**
 * Клас для обчислення кількості входжень цифр
 */
class DigitCalculator {

    public Map<Integer, Integer> calculate(int number) {
        Map<Integer, Integer> result = new HashMap<>();
        int num = Math.abs(number);

        if (num == 0) {
            result.put(0, 1);
            return result;
        }

        while (num > 0) {
            int digit = num % 10;
            result.put(digit, result.getOrDefault(digit, 0) + 1);
            num /= 10;
        }

        return result;
    }
}


/**
 * Збереження/відновлення колекції результатів
 */
class Storage {

    public static void save(List<Map<Integer, Integer>> list, String file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
            System.out.println("Дані збережено.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Map<Integer, Integer>> load(String file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Map<Integer, Integer>>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}


public class zavd3 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DigitCalculator calculator = new DigitCalculator();

        List<Map<Integer, Integer>> collection = new ArrayList<>();

        System.out.print("Введіть число: ");
        int number = scanner.nextInt();

        // Обчислення
        Map<Integer, Integer> result = calculator.calculate(number);

        // Додаємо в колекцію
        collection.add(result);

        // Factory Method
        ViewFactory factory = new TextViewFactory();
        ResultView view = factory.createView(result);

        // Вивід
        view.display();

        // Серіалізація колекції
        Storage.save(collection, "results.ser");

        // Відновлення
        List<Map<Integer, Integer>> loaded = Storage.load("results.ser");

        System.out.println("\n=== Відновлені дані ===");
        for (Map<Integer, Integer> r : loaded) {
            ResultView v = factory.createView(r);
            v.display();
        }

        scanner.close();
    }
}