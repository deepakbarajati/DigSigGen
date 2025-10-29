package modulation;

public class PCMModulator {
    /**
     * Modulates analog input samples using Pulse Code Modulation (PCM).
     * This method quantizes the input analogSamples into discrete levels,
     * then encodes each quantized level as an integer code.
     *
     * @param analogSamples Array of analog input samples
     * @return int[] quantized digital values (levels)
     */
    public int[] modulate(double[] analogSamples) {
        if (analogSamples == null || analogSamples.length == 0) {
            return new int[0];
        }

        int numLevels = 16; // Number of quantization levels (can be 2^n for n bits)
        double maxVal = Double.NEGATIVE_INFINITY;
        double minVal = Double.POSITIVE_INFINITY;

        // Find min and max of input samples
        for (double sample : analogSamples) {
            if (sample > maxVal) maxVal = sample;
            if (sample < minVal) minVal = sample;
        }

        double range = maxVal - minVal;
        double stepSize = range / numLevels;

        int[] quantized = new int[analogSamples.length];

        for (int i = 0; i < analogSamples.length; i++) {
            // Normalize sample to quantization levels
            int level = (int) ((analogSamples[i] - minVal) / stepSize);

            // Clamp to max level
            if (level >= numLevels) {
                level = numLevels - 1;
            }
            quantized[i] = level;
        }

        return quantized;
    }
}
