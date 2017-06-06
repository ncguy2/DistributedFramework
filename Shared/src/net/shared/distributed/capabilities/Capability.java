package net.shared.distributed.capabilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Capability {

    /** @return The internal name for the capability */
    String name();

    /** @return The class type for the invocation process to invoke on the host */
    Class<? extends CapabilityFunction> hostFunction() default CapabilityFunction.NoFunction.class;
    /** @return The class type for the invocation process to invoke on the node */
    Class<? extends CapabilityFunction> nodeFunction() default CapabilityFunction.NoFunction.class;

    /** @return Whether the same CapabilityFunction instance should be used for all requests for this capability */
    boolean cache() default true;

    enum Side {
        NONE,
        HOST,
        NODE,
        ;
    }

}
