package gr.spinellis.ckjm;

import org.apache.bcel.classfile.JavaClass;

public class ClassParser {
    public static void parseClass(JavaClass jc, ClassMetricsContainer cm) {
        if (jc != null) {
            ClassVisitor visitor = new ClassVisitor(jc, cm);
            visitor.start();
            visitor.end();
        }
    }
}
