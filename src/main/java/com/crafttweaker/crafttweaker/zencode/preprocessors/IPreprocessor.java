package com.crafttweaker.crafttweaker.zencode.preprocessors;

import com.crafttweaker.crafttweaker.zencode.ScriptLoader;

public interface IPreprocessor {
    
    /**
     * The name. Preprocessors look like #name
     */
    String getName();
    
    /**
     * Do things
     */
    void accept(ScriptLoader loader, String line);
    
    /**
     * These preprocessors get the chance to prevent a file from being executed at all
     */
    default boolean allowScriptToBeExecuted(ScriptLoader loader, String line) {
        return true;
    }
    
}
