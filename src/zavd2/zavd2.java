package zavd2;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Клас для зберігання вхідних даних та результатів обчислення.
 * Реалізує Serializable для можливості серіалізації об'єкта.
 */
class DigitData implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Вхідне число */
    int number;

    /** Карта для зберігання кількості входжень цифр */
    Map<Integer, Integer> digitCount;

    /** Тимчасове поле, яке не підлягає серіалізації */
    transient String info;

    /**
     * Конструктор ініціалізації об'єкта.
     * @param number число для аналізу
     */
    public DigitData(int number) {
        this.number = number;
        this.digitCount = new HashMap<>();
        this.info = "Це службове поле (не зберігається)";
    }
}

/**
 * Клас, що виконує обчислення (підрахунок цифр).
 * Використовує агрегування DigitData.
 */
class DigitCalculator {

    /** Об'єкт з даними */
    private DigitData data;

    /**
     * Конструктор.
     * @param data об'єкт з вхідними даними
     */
    public DigitCalculator(DigitData data) {
        this.data = data;
    }

    /**
     * Виконує підрахунок входжень кожної цифри.
     */
    public void calculate() {
        int num = Math.abs(data.number);

        if (num == 0) {
            data.digitCount.put(0, 1);
            return;
        }

        while (num > 0) {
            int digit = num % 10;
            data.digitCount.put(digit, data.digitCount.getOrDefault(digit, 0) + 1);
            num /= 10;
        }
    }

    /**
     * Виводить результати обчислень у консоль.
     */
    public void printResult() {
        System.out.println("Результати:");
        for (Map.Entry<Integer, Integer> entry : data.digitCount.entrySet()) {
            System.out.println("Цифра " + entry.getKey() + ": " + entry.getValue());
        }
    }
}

/**
 * Клас для серіалізації та десеріалізації об'єктів.
 */
class Serializer {

    /**
     * Зберігає об'єкт у файл.
     * @param data об'єкт для збереження
     * @param filename ім'я файлу
     */
    public static void save(DigitData data, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
            System.out.println("Дані збережено.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Завантажує об'єкт із файлу.
     * @param filename ім'я файлу
     * @return відновлений об'єкт
     */
    public static DigitData load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            System.out.println("Дані завантажено.");
            return (DigitData) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

/**
 * Головний клас для тестування програми.
 */
public class zavd2 {

    /**
     * Точка входу в програму.
     * Виконує введення даних, обчислення, серіалізацію та тестування.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть ціле число: ");
        int number = scanner.nextInt();

        DigitData data = new DigitData(number);

        DigitCalculator calculator = new DigitCalculator(data);
        calculator.calculate();
        calculator.printResult();

        Serializer.save(data, "data.ser");

        data = null;

        DigitData loadedData = Serializer.load("data.ser");

        if (loadedData != null) {
            System.out.println("\nПісля десеріалізації:");
            DigitCalculator calc2 = new DigitCalculator(loadedData);
            calc2.printResult();

            System.out.println("Значення transient поля: " + loadedData.info);
        }

        scanner.close();
    }
}