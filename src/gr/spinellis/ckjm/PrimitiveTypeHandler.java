package gr.spinellis.ckjm;

import gr.spinellis.ckjm.TypeHandler;
import org.apache.bcel.generic.Type;

class PrimitiveTypeHandler implements TypeHandler {
    private Type type;

    public PrimitiveTypeHandler(Type type) {
        this.type = type;
    }

    @Override
    public String getClassName() {
        return "java.PRIMITIVE";
    }
}