package com.crafttweaker.crafttweaker.zencode.preprocessors;

import com.crafttweaker.crafttweaker.zencode.ZCLoader;
import org.openzen.zencode.shared.SourceFile;

public interface IPreprocessor {
    
    /**
     * The name. Preprocessors look like #name
     */
    String getName();
    
    /**
     * These preprocessors get the chance to prevent a file from being executed at all
     * Will be executed first
     */
    default boolean allowScriptToBeExecuted(ZCLoader loader, String line) {
        return true;
    }
    
    /**
     * Do things
     * Will be executed second
     */
    default void accept(ZCLoader loader, String line) {
    
    }
    
    /**
     * Allows for the modification of the source file.
     * You can either update the actual source file (which might change files) or return a new Source file.
     *
     * Will be executed last.
     * If another preprocessor has already modified that file then you will receive that one instead.
     * This means that it might be possible that the file given here does not even contain the preprocessor line given.
     *
     * The line parameter will still contain the original preprocessor call, though!
     */
    default SourceFile modifySourceFile(ZCLoader loader, String line, SourceFile file) {
        return file;
    }
    
}
