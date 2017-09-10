package net.shared.distributed.utils;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class ReflectionHelper {

    public static <T extends Annotation> Optional<T> GetAnnotation(Class<?> cls, Class<T> t) {
        if(cls.isAnnotationPresent(t))
            return Optional.of(cls.getAnnotation(t));
        return Optional.empty();
    }

    public static Set<Class<?>> GetAnnotatedTypes(Class<? extends Annotation> annotation) {
        Reflections ref = new Reflections();
        Set<Class<?>> typesAnnotatedWith = ref.getTypesAnnotatedWith(annotation);
        return typesAnnotatedWith;
    }

    public static Stream<Class<?>> GetAnnotatedTypeStream(Class<? extends Annotation> annotation) {
        return GetAnnotatedTypes(annotation).stream();
    }

    public static <T> Optional<T> Build(Class<T> cls, Object... args) {
        Constructor<?> ctor = null;
        try {
            if (args.length > 0) {
                Class<?>[] argTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++)
                    argTypes[i] = args[i].getClass();
                ctor = cls.getDeclaredConstructor(argTypes);
            } else {
                ctor = cls.getDeclaredConstructor();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

        if(ctor == null)
            return Optional.empty();

        try {
            Object o = ctor.newInstance(args);
            if(cls.isInstance(o))
                return Optional.of((T) o);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static int GetHashcode(Object obj) {
        return GetClassHashcode(obj.getClass());
    }

    public static int GetClassHashcode(Class<?> cls) {
        return cls.getCanonicalName().hashCode();
    }

}
