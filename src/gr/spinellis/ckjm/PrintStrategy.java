package gr.spinellis.ckjm;

import org.apache.bcel.classfile.*;
import java.util.*;
import java.io.*;


public interface PrintStrategy {
    boolean shouldPrintMetrics(ClassMetrics metrics);
}

