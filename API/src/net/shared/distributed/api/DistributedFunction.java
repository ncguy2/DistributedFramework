package net.shared.distributed.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The tagged class should invoke the respective capability function
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DistributedFunction {

    /**
     * @return The capability function class
     */
    Class<?> value();

}
