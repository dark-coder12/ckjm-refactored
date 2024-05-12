package gr.spinellis.ckjm;

public class PrintVisitedOnlyStrategy implements PrintStrategy {
    @Override
    public boolean shouldPrintMetrics(ClassMetrics metrics) {
        return metrics.javaInformation.isVisited();
    }
}