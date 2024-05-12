

package gr.spinellis.ckjm;

import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;
import org.apache.bcel.util.*;
import java.util.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

class MethodVisitor extends EmptyVisitor implements Visitor {
    private MethodGen methodGen;
    private ConstantPoolGen constantPool;
    private ClassVisitor classVisitor;
    private ClassMetrics classMetrics;

    public MethodVisitor(MethodGen method, ClassVisitor classVisitor) {
        methodGen = method;
        this.classVisitor = classVisitor;
        constantPool = methodGen.getConstantPool();
        classMetrics = classVisitor.getMetrics();
    }

    public void start() {
        if (!methodGen.isAbstract() && !methodGen.isNative()) {
            for (InstructionHandle instructionHandle = methodGen.getInstructionList().getStart();
                 instructionHandle != null; instructionHandle = instructionHandle.getNext()) {
                Instruction instruction = instructionHandle.getInstruction();

                if (!visitInstruction(instruction)) {
                    instruction.accept(this);
                }
            }
            updateExceptionHandlers();
        }
    }

    @Override
    public void end() {
    }

    private boolean visitInstruction(Instruction instruction) {
        short opcode = instruction.getOpcode();
        return ((InstructionConstants.INSTRUCTIONS[opcode] != null) &&
                !(instruction instanceof ConstantPushInstruction) &&
                !(instruction instanceof ReturnInstruction));
    }

    public void visitLocalVariableInstruction(LocalVariableInstruction instruction) {
        if (instruction.getOpcode() != Constants.IINC) {
            classVisitor.registerCoupling(instruction.getType(constantPool));
        }
    }

    public void visitArrayInstruction(ArrayInstruction instruction) {
        classVisitor.registerCoupling(instruction.getType(constantPool));
    }

    public void visitFieldInstruction(FieldInstruction instruction) {
        classVisitor.registerFieldAccess(instruction.getClassName(constantPool), instruction.getFieldName(constantPool));
        classVisitor.registerCoupling(instruction.getFieldType(constantPool));
    }

    public void visitInvokeInstruction(InvokeInstruction instruction) {
        Type[] argTypes = instruction.getArgumentTypes(constantPool);
        for (Type argType : argTypes) {
            classVisitor.registerCoupling(argType);
        }
        classVisitor.registerCoupling(instruction.getReturnType(constantPool));
        classVisitor.incrementRFC(
                instruction.getClassName(constantPool),
                instruction.getMethodName(constantPool),
                argTypes
        );
    }

    public void visitINSTANCEOF(INSTANCEOF instruction) {
        classVisitor.registerCoupling(instruction.getType(constantPool));
    }

    public void visitCHECKCAST(CHECKCAST instruction) {
        classVisitor.registerCoupling(instruction.getType(constantPool));
    }

    public void visitReturnInstruction(ReturnInstruction instruction) {
        classVisitor.registerCoupling(instruction.getType(constantPool));
    }

    private void updateExceptionHandlers() {
        CodeExceptionGen[] exceptionHandlers = methodGen.getExceptionHandlers();

        for (CodeExceptionGen exceptionHandler : exceptionHandlers) {
            Type exceptionType = exceptionHandler.getCatchType();
            if (exceptionType != null) {
                classVisitor.registerCoupling(exceptionType);
            }
        }
    }
}
