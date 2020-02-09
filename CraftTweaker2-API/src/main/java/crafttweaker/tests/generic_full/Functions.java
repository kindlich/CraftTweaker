package crafttweaker.tests.generic_full;

import crafttweaker.annotations.*;
import stanhebben.zenscript.annotations.*;

public interface Functions {
    
    @ZenClass("crafttweaker.tests.generics_tests.Mapper")
    @ZenRegister
    interface Mapper<T, U> {
        
        U map(T t);
    }
    
    @ZenClass("crafttweaker.tests.generics_tests.Predicate")
    @ZenRegister
    interface Predicate<T> {
        
        boolean test(T t);
    }
    
    @ZenClass("crafttweaker.tests.generics_tests.Consumer")
    @ZenRegister
    interface Consumer<T> {
        
        void accept(T t);
    }
}