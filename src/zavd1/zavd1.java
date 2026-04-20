package zavd1;
public class zavd1 {
    public static void main(String[] args) {

        args = new String[]{"banana","apple","orange"};

        System.out.println("Аргументи командного рядка:");

        if (args.length == 0) {
            System.out.println("Аргументи відсутні.");
        } else {
            for (int i = 0; i < args.length; i++) {
                System.out.println("Аргумент " + i + ": " + args[i]);
            }
        }
    }
}