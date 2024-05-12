package gr.spinellis.ckjm;

import org.apache.bcel.generic.Type;

class DefaultTypeHandler implements TypeHandler {
    private Type type;

    public DefaultTypeHandler(Type type) {
        this.type = type;
    }

    @Override
    public String getClassName() {
        return type.toString();
    }
}