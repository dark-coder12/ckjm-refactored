package gr.spinellis.ckjm;

public class IntegerMetric extends Metric {
    public IntegerMetric() {
        super();
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}