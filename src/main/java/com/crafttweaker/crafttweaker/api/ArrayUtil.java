package com.crafttweaker.crafttweaker.api;

import org.jetbrains.annotations.Contract;

import java.lang.reflect.Array;
import java.util.Arrays;

public final class ArrayUtil {
    
    private ArrayUtil() {
    }
    
    @Contract(value = "null -> null;!null->new", pure = true)
    public static <T> T[] mirror(T[] array) {
        if(array == null)
            return null;
        
        final T[] out = Arrays.copyOf(array, array.length);
        
        for(int index = 0; index < array.length; index++) {
            out[index] = array[array.length - index - 1];
            out[array.length - index - 1] = array[index];
        }
        
        return out;
    }
}
