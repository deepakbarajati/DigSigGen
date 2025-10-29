package encoding;

public class NRZIEncoder implements LineEncoder {
    @Override
    public int[] encode(int[] bitStream) {
        int[] signal = new int[bitStream.length];
        int lastLevel = -1;
        for (int i = 0; i < bitStream.length; i++) {
            if (bitStream[i] == 1) {
                lastLevel *= -1;
            }
            signal[i] = lastLevel;
        }
        return signal;
    }
}
