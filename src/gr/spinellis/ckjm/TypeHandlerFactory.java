package gr.spinellis.ckjm;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.Type;

import javax.lang.model.type.ArrayType;

class TypeHandlerFactory {
    static TypeHandler createTypeHandler(Type type) {
        if (type.getType() <= Constants.T_VOID) {
            return new PrimitiveTypeHandler(type);
        } else if (type instanceof ArrayType) {
            return new ArrayTypeHandler((Type) type);
        } else {
            return new DefaultTypeHandler(type);
        }
    }
}
