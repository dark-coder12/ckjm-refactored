package gr.spinellis.ckjm;

public class PrintPublicVisitedStrategy implements PrintStrategy {
    @Override
    public boolean shouldPrintMetrics(ClassMetrics metrics) {
        return (MetricsFilter.includeAll() || metrics.javaInformation.isPublic()) && (metrics.javaInformation.isVisited());
    }
}

