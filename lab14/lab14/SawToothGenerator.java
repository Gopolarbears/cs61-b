package lab14;

import edu.princeton.cs.algs4.StdAudio;
import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        double next = normalize(state % period);
        state = state + 1;
        return next;
    }

    private double normalize(int num) {
        return (double) num * 2/ (period - 1) - 1;
    }
}
