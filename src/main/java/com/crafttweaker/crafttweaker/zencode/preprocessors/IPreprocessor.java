package com.crafttweaker.crafttweaker.zencode.preprocessors;

import com.crafttweaker.crafttweaker.zencode.ZCLoader;

public interface IPreprocessor {
    
    /**
     * The name. Preprocessors look like #name
     */
    String getName();
    
    /**
     * Do things
     */
    void accept(ZCLoader loader, String line);
    
    /**
     * These preprocessors get the chance to prevent a file from being executed at all
     */
    default boolean allowScriptToBeExecuted(ZCLoader loader, String line) {
        return true;
    }
    
}
