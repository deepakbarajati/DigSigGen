package encoding;

public class NRZLEncoder implements LineEncoder {
    @Override
    public int[] encode(int[] bitStream) {
        int[] signal = new int[bitStream.length];
        for (int i = 0; i < bitStream.length; i++) {
            signal[i] = bitStream[i] == 1 ? 1 : -1;
        }
        return signal;
    }
}
