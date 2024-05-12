package gr.spinellis.ckjm;

import java.util.HashSet;

public class StringMetric extends Metric {
    private HashSet<String> values;

    public StringMetric() {
        super();
        this.values = new HashSet<>();
    }

    public void addValue(String value) {
        this.values.add(value);
    }

    public int getCount() {
        return this.values.size();
    }

    @Override
    public String toString() {
        return Integer.toString(getCount());
    }
}
