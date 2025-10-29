package encoding;

public class DiffManchesterEncoder implements LineEncoder {
    @Override
    public int[] encode(int[] bitStream) {
        int[] signal = new int[bitStream.length * 2];
        int current = 1; // Initial level
        for (int i = 0; i < bitStream.length; i++) {
            if (bitStream[i] == 0) current *= -1;
            signal[2 * i] = current;
            signal[2 * i + 1] = -current;
        }
        return signal;
    }
}

