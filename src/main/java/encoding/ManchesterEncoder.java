package encoding;

public class ManchesterEncoder implements LineEncoder {
    @Override
    public int[] encode(int[] bitStream) {
        int[] signal = new int[bitStream.length * 2];
        for (int i = 0; i < bitStream.length; i++) {
            signal[2 * i] = bitStream[i] == 1 ? 1 : -1;
            signal[2 * i + 1] = bitStream[i] == 1 ? -1 : 1;
        }
        return signal;
    }
}

