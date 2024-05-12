package gr.spinellis.ckjm;

import java.io.OutputStream;
import java.io.PrintStream;

public class PrintPlainResults implements CkjmOutputHandler {
    private final OutputStream outputStream;

    public PrintPlainResults(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void processClassMetrics(String className, ClassMetrics classMetrics) {
        if (classMetrics != null) {
            try (PrintStream printStream = new PrintStream(outputStream)) {
                printStream.println(String.format("%s %s", className, classMetrics.toString()));
            }
        } else {
            System.err.println("Error: ClassMetrics is null for class " + className);
        }
    }
}