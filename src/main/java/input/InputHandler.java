package input;

import java.util.Scanner;

public class InputHandler {
    private Scanner scanner = new Scanner(System.in);

    // Read digital input as a bit stream from console
    public int[] getDigitalInput() {
        System.out.println("Enter bit stream (e.g., 101001): ");
        String input = scanner.nextLine();
        int[] bits = new int[input.length()];
        for (int i = 0; i < input.length(); i++) {
            bits[i] = input.charAt(i) - '0';
        }
        return bits;
    }

    // Read analog input values from console for PCM/DM input
    public double[] getAnalogInput() {
        System.out.println("Enter number of analog samples: ");
        int n = Integer.parseInt(scanner.nextLine());

        double[] samples = new double[n];
        System.out.println("Enter each analog sample separated by Enter:");
        for (int i = 0; i < n; i++) {
            samples[i] = Double.parseDouble(scanner.nextLine());
        }
        return samples;
    }

    // Ask user for input type and return either digital or analog input based on choice
    public Object getInputBasedOnType() {
        System.out.println("Choose input type: 1 for Digital, 2 for Analog (PCM/DM): ");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            return getDigitalInput();
        } else if ("2".equals(choice)) {
            return getAnalogInput();
        } else {
            System.out.println("Invalid choice. Defaulting to digital input.");
            return getDigitalInput();
        }
    }
}
