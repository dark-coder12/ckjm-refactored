package gr.spinellis.ckjm;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Type;

public interface Visitor {
    void start();
    void end();
}
