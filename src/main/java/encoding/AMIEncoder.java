package encoding;

public class AMIEncoder implements LineEncoder {
    @Override
    public int[] encode(int[] bitStream) {
        int[] signal = new int[bitStream.length];
        int polarity = 1;
        for (int i = 0; i < bitStream.length; i++) {
            if (bitStream[i] == 1) {
                signal[i] = polarity;
                polarity *= -1;
            } else {
                signal[i] = 0;
            }
        }
        return signal;
    }
}

