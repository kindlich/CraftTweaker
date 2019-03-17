package com.crafttweaker.crafttweaker.zencode.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SimpleBracketRegistration {
    
    String[] loaders() default {"crafttweaker"};
    
    String name();
}
