package com.crafttweaker.crafttweaker.api;

import org.jetbrains.annotations.Contract;

import java.lang.reflect.Array;

public final class ArrayUtil {
    
    private ArrayUtil() {
    }
    
    @Contract("null -> null")
    @SuppressWarnings("unchecked")
    public static <T> T[] mirror(T[] array) {
        if(array == null)
            return null;
        
        //final T[] out = (T[]) new Object[array.length];
        final T[] out = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        
        for(int index = 0; index < array.length; index++) {
            out[index] = array[array.length - index - 1];
            out[array.length - index - 1] = array[index];
        }
        
        return out;
    }
}
