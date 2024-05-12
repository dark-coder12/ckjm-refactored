package gr.spinellis.ckjm;

import java.util.HashSet;

public abstract class Metric {
    protected int value;

    public Metric() {
        this.value = 0;
    }

    public void increment() {
        this.value++;
    }

    public int getValue() {
        return this.value;
    }

    public abstract String toString();
}