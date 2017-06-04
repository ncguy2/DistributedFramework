package net.shared.distributed;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class TypeFunctionHandler {

    protected List<TypeFunction<?>> typeFunctions;

    public TypeFunctionHandler() {
        typeFunctions = new ArrayList<>();
    }

    public <T> void AddTypeHandler(Class<T> cls, BiConsumer<T, Socket> func) {
        AddTypeHandler(new TypeFunction<>(cls, func));
    }
    public <T> void AddTypeHandler(TypeFunction<T> typeFunc) {
        typeFunctions.add(typeFunc);
    }

    public void AcceptObject(Object obj, Socket origin) {
        Optional<TypeFunction<?>> first = typeFunctions.stream()
                .filter(typeFunc -> typeFunc.cls.equals(obj.getClass()))
                .findFirst();
        first.ifPresent(func -> func.AcceptCast(obj, origin));
    }

    public static class TypeFunction<T> {
        public Class<T> cls;
        public BiConsumer<T, Socket> func;

        public TypeFunction(Class<T> cls, BiConsumer<T, Socket> func) {
            this.cls = cls;
            this.func = func;
        }

        public void AcceptCast(Object obj, Socket s) {
            this.Accept((T) obj, s);
        }

        public void Accept(T t, Socket s) {
            this.func.accept(t, s);
        }

    }

}
