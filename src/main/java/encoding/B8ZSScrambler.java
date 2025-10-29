package encoding;

public class B8ZSScrambler {
    /**
     * Scrambles the AMI-encoded input using B8ZS.
     * @param amiSignal An integer array representing AMI-encoded signal (e.g., -1, 0, +1)
     * @return int[] scrambled signal (same length as input)
     */
    public int[] scramble(int[] amiSignal) {
        int[] result = amiSignal.clone();
        int n = amiSignal.length;
        int lastPulse = 1; // Initial pulse polarity, can be +1 or -1

        // Find the last pulse before substitutions (for start)
        for (int v : amiSignal) {
            if (v != 0) {
                lastPulse = v;
            }
        }

        int i = 0;
        while (i <= n - 8) {
            // Check for 8 consecutive zeros
            boolean eightZeros = true;
            for (int j = 0; j < 8; j++) {
                if (result[i + j] != 0) {
                    eightZeros = false;
                    break;
                }
            }
            if (eightZeros) {
                // Determine B8ZS substitution
                // The pattern to insert is 0 0 0 V B 0 V B, where:
                // V = violation (same polarity as previous pulse)
                // B = bipolar (opposite polarity to previous pulse)
                int v = lastPulse;
                int b = -lastPulse;

                result[i + 3] = v;
                result[i + 4] = b;
                result[i + 5] = 0;
                result[i + 6] = v;
                result[i + 7] = b;

                // Update lastPulse for further substitutions
                lastPulse = b;
                i += 8; // Skip ahead
            } else {
                // Track polarity for next substitution
                if (result[i] != 0) {
                    lastPulse = result[i];
                }
                i++;
            }
        }
        return result;
    }
}
