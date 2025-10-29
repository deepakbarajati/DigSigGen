package encoding;

public class HDB3Scrambler {
    /**
     * Scrambles the AMI-encoded input using HDB3.
     * @param amiSignal An integer array representing AMI-encoded signal (-1, 0, +1)
     * @return int[] scrambled signal (same length as input)
     */
    public int[] scramble(int[] amiSignal) {
        int[] result = amiSignal.clone();
        int n = result.length;

        int pulseCount = 0;    // Count of non-zero pulses since last substitution
        int lastPulse = 1;     // Polarity of last non-zero pulse
        int zeroCount = 0;     // Count of consecutive zeros

        for (int i = 0; i < n; i++) {
            if (result[i] == 0) {
                zeroCount++;
            } else {
                pulseCount++;
                zeroCount = 0;
                lastPulse = result[i];
            }

            // When 4 consecutive zeros found, apply substitution
            if (zeroCount == 4) {
                // Determine substitution pattern
                // If pulseCount is even, use B00V pattern
                // If pulseCount is odd, use 000V pattern
                int v, b;
                if (pulseCount % 2 == 0) {
                    b = -lastPulse; // Bipolar pulse opposite to last pulse
                    v = b;          // Violation pulse same polarity as B here to ensure violation
                    result[i - 3] = b;
                    result[i - 2] = 0;
                    result[i - 1] = 0;
                    result[i] = v;
                    pulseCount = 0; // Reset pulse count after substitution
                    lastPulse = v;
                } else {
                    v = lastPulse;  // Violation pulse same polarity as last pulse
                    result[i - 3] = 0;
                    result[i - 2] = 0;
                    result[i - 1] = 0;
                    result[i] = v;
                    pulseCount = 1; // Violation pulse counts as one pulse
                    lastPulse = v;
                }
                zeroCount = 0; // Reset zero count after substitution
            }
        }
        return result;
    }
}
