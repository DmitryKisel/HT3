
public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        // This check for arguments presence
        if (args.length == 2) {
            for (String arg : args) {
                if (manager.isCorrect(arg)) {
                    continue;
                } else {
                    System.out.println("[Incorrect file extension! Please run application like in example]" + "\n" +
                            "Example: java main input.txt output.txt");
                    System.exit(0);
                }
            }
            manager.run(args[0], args[1]);
        }
        else {
            System.out.println("[Arguments error! Please run application with 2 arguments like in example]" + "\n" +
                    "Example: java main input.txt output.txt");
            System.exit(0);
        }
    }
}
