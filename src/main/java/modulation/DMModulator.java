package modulation;

public class DMModulator {

    /**
     * Modulates an analog input (double[]) using Differential Modulation (Delta Modulation).
     * Produces binary output array of 0/1 representing increase or decrease in signal.
     *
     * @param analogSamples Array of analog signal amplitude samples
     * @return int[] binary modulated data stream
     */
    public int[] modulate(double[] analogSamples) {
        if (analogSamples == null || analogSamples.length == 0) {
            return new int[0];
        }

        int[] modulated = new int[analogSamples.length];
        double stepSize = 0.1; // quantization step size
        double prevApprox = analogSamples[0]; // initial approximation

        // First modulated bit does not represent difference, so set to 0 by convention
        modulated[0] = 0;

        for (int i = 1; i < analogSamples.length; i++) {
            if (analogSamples[i] >= prevApprox) {
                modulated[i] = 1;
                prevApprox += stepSize;
            } else {
                modulated[i] = 0;
                prevApprox -= stepSize;
            }
        }

        return modulated;
    }
}
