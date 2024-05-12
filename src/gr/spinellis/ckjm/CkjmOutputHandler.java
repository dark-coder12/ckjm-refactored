package gr.spinellis.ckjm;

import java.io.OutputStream;
import java.io.PrintStream;

public interface CkjmOutputHandler {
    void processClassMetrics(String className, ClassMetrics classMetrics);
}
