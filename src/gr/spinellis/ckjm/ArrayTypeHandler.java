package gr.spinellis.ckjm;

import org.apache.bcel.generic.Type;

import javax.lang.model.type.ArrayType;

class ArrayTypeHandler implements TypeHandler {
    private Type elementType;

    public ArrayTypeHandler(Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public String getClassName() {
        return getClassName(elementType);
    }

    private String getClassName(Type type) {
        TypeHandler handler = TypeHandlerFactory.createTypeHandler(type);
        return handler.getClassName();
    }
}