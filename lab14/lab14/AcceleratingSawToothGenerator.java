package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int state;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        state = 0;
    }

    @Override
    public double next() {
        double next = normalize(state % period);
        state = state + 1;
        if (state == period) {
            period = (int) (period * factor);
            state = 0;
        }
        return next;
    }

    private double normalize(int num) {
        double next = (double) num * 2/ (period - 1) - 1;
        return next;
    }
}
