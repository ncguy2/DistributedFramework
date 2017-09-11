package net.shared.distributed.api;

import java.awt.*;
import java.util.Optional;

/**
 * Builder structure for UI control widgets
 */
public interface ControlBuilder {

    /**
     * @param type The UI type to query
     * @return Whether type is supported
     */
    default boolean Supports(Class<?> type) {
        return type != ControlTypes.NONE;
    }

    /**
     * The builder function
     * @param type The UI type
     * @param <T> The resulting data type, determined by the UI type
     * @return The UI widget instance
     */
    <T> Optional<T> Build(Class<T> type);

    /**
     * Default UI widget builder
     * - Supports all
     * - Builds none
     */
    public static class NoneBuilder implements ControlBuilder {

        @Override
        public boolean Supports(Class<?> type) {
            return true;
        }

        @Override
        public <T> Optional<T> Build(Class<T> type) {
            return Optional.empty();
        }
    }

    /**
     * Predefined control types
     */
    public static class ControlTypes {
        public static final Class<Object> NONE = Object.class;
        public static final Class<Component> SWING = Component.class;
        public static final Class<String> HTTP = String.class;
    }

}
