package main;

import input.InputHandler;
import encoding.LineEncoder;
import encoding.NRZLEncoder;
import encoding.NRZIEncoder;
import encoding.ManchesterEncoder;
import encoding.DiffManchesterEncoder;
import encoding.AMIEncoder;
import encoding.B8ZSScrambler;
import encoding.HDB3Scrambler;
import modulation.PCMModulator;
import modulation.DMModulator;
import util.PalindromeFinder;
import visualizer.SignalVisualizer;

import java.util.Scanner;

public class DigitalSignalGeneratorApp {

    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digital Signal Generator");
        System.out.println("------------------------");
        System.out.println("Select input type:");
        System.out.println("1. Digital Input (bitstream)");
        System.out.println("2. Analog Input (PCM or DM)");

        int inputType = scanner.nextInt();
        scanner.nextLine(); // consume newline

        int[] digitalData = null;
        double[] analogData = null;
        int[] encodedSignal = null;

        if (inputType == 1) {
            digitalData = inputHandler.getDigitalInput();
        } else if (inputType == 2) {
            analogData = inputHandler.getAnalogInput();
            System.out.println("Select modulation:");
            System.out.println("1. Pulse Code Modulation (PCM)");
            System.out.println("2. Delta Modulation (DM)");
            int modulationChoice = scanner.nextInt();
            scanner.nextLine();

            if (modulationChoice == 1) {
                PCMModulator pcm = new PCMModulator();
                digitalData = pcm.modulate(analogData);
            } else if (modulationChoice == 2) {
                DMModulator dm = new DMModulator();
                digitalData = dm.modulate(analogData);
            }
        } else {
            System.out.println("Invalid input choice. Exiting.");
            System.exit(0);
        }

        System.out.println("Choose line encoding scheme:");
        System.out.println("1. NRZ-L");
        System.out.println("2. NRZ-I");
        System.out.println("3. Manchester");
        System.out.println("4. Differential Manchester");
        System.out.println("5. AMI");

        int encodingChoice = scanner.nextInt();
        scanner.nextLine();

        LineEncoder encoder;
        switch (encodingChoice) {
            case 1: encoder = new NRZLEncoder(); break;
            case 2: encoder = new NRZIEncoder(); break;
            case 3: encoder = new ManchesterEncoder(); break;
            case 4: encoder = new DiffManchesterEncoder(); break;
            case 5:
                encoder = new AMIEncoder();
                System.out.println("Do you want scrambling? (yes/no):");
                String scrambleResponse = scanner.nextLine();
                if (scrambleResponse.equalsIgnoreCase("yes")) {
                    System.out.println("Select scrambling scheme:");
                    System.out.println("1. B8ZS");
                    System.out.println("2. HDB3");
                    int scrambleChoice = scanner.nextInt();
                    scanner.nextLine();

                    int[] amiEncoded = encoder.encode(digitalData);
                    if (scrambleChoice == 1) {
                        B8ZSScrambler b8zs = new B8ZSScrambler();
                        encodedSignal = b8zs.scramble(amiEncoded);
                    } else if (scrambleChoice == 2) {
                        HDB3Scrambler hdb3 = new HDB3Scrambler();
                        encodedSignal = hdb3.scramble(amiEncoded);
                    } else {
                        System.out.println("Invalid scramble choice. Proceeding without scrambling.");
                        encodedSignal = amiEncoded;
                    }
                } else {
                    encodedSignal = encoder.encode(digitalData);
                }
                break;
            default:
                System.out.println("Invalid encoding choice. Exiting.");
                System.exit(0);
                return;
        }

        if (encodingChoice != 5) {
            encodedSignal = encoder.encode(digitalData);
        }

        // Find longest palindrome in digital bit stream as string
        StringBuilder bitStrBuilder = new StringBuilder();
        for (int bit : digitalData) {
            bitStrBuilder.append(bit);
        }
        String longestPalindrome = PalindromeFinder.longestPalindrome(bitStrBuilder.toString());
        System.out.println("Longest palindrome in data stream: " + longestPalindrome);

        // Visualize encoded signal
        SignalVisualizer visualizer = new SignalVisualizer(encodedSignal);
        visualizer.repaint();

        // Extra Credit: Decode option (optional)
        System.out.println("Do you want to decode the signal? (yes/no):");
        String decodeResponse = scanner.nextLine();
        if (decodeResponse.equalsIgnoreCase("yes")) {
            // Implement decoding logic corresponding to chosen encoder here
            System.out.println("Decoding feature is under development.");
        }

        scanner.close();
    }
}
