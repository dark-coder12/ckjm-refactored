package gr.spinellis.ckjm;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.ClassParser;

import java.io.IOException;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.ClassParser;

import java.io.IOException;

public class ClassFileLoader {
    public static JavaClass loadClass(String clspec) {
        int spc;
        JavaClass jc = null;

        if ((spc = clspec.indexOf(' ')) != -1) {
            String jar = clspec.substring(0, spc);
            clspec = clspec.substring(spc + 1);
            try {
                jc = new ClassParser(jar, clspec).parse();
            } catch (IOException e) {
                System.err.println("Error loading " + clspec + " from " + jar + ": " + e);
            }
        } else {
            try {
                jc = new ClassParser(clspec).parse();
            } catch (IOException e) {
                System.err.println("Error loading " + clspec + ": " + e);
            }
        }
        return jc;
    }
}
