package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        double weirdState = normalize(state % period);
        state = state + 1;
        return (double) weirdState * 2 / period - 1;
    }

    private double normalize(int num) {
        double weirdState = state & (state >>> 3) % period;
        return weirdState;
    }
}
