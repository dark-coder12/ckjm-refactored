package gr.spinellis.ckjm;

import gr.spinellis.ckjm.CkjmOutputHandler;
import gr.spinellis.ckjm.ClassMetrics;
import gr.spinellis.ckjm.PrintStrategy;

import java.util.Map;

public class MetricsPrinter {
    private PrintStrategy printStrategy;

    public MetricsPrinter(PrintStrategy printStrategy) {
        this.printStrategy = printStrategy;
    }

    public void printMetrics(CkjmOutputHandler handler, Map<String, ClassMetrics> classMetricsMap) {
        for (Map.Entry<String, ClassMetrics> entry : classMetricsMap.entrySet()) {
            String className = entry.getKey();
            ClassMetrics metrics = entry.getValue();
            if (printStrategy.shouldPrintMetrics(metrics)) {
                handler.processClassMetrics(className, metrics);
            }
        }
    }
}
