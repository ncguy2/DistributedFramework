package net.shared.distributed.api;

import java.awt.*;
import java.util.Optional;

public interface ControlBuilder {

    default boolean Supports(Class<?> type) {
        return type != ControlTypes.NONE;
    }

    <T> Optional<T> Build(Class<T> type);

    public static class NoneBuilder implements ControlBuilder {

        @Override
        public <T> Optional<T> Build(Class<T> type) {
            return Optional.empty();
        }
    }

    public static class ControlTypes {
        public static final Class<Object> NONE = Object.class;
        public static final Class<Component> SWING = Component.class;
        public static final Class<String> HTTP = String.class;
    }

}
