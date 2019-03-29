package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.api.CraftTweakeAPI;
import com.crafttweaker.crafttweaker.zencode.preprocessors.IPreprocessor;

public class PriorityPreprocessor implements IPreprocessor {
    
    public static final PriorityPreprocessor INSTANCE = new PriorityPreprocessor();
    
    private PriorityPreprocessor() {
    
    }
    
    @Override
    public String getName() {
        return "priority";
    }
    
    @Override
    public void accept(ZCLoader loader, String line) {
        CraftTweakeAPI.logger.logDebug("Priority preprocessor hit with " + line);
        if(!isValid(line))
            CraftTweakeAPI.logger.logError("Invalid line, must be an integer: " + line + ", falling back to 0");
        
    }
    
    public int getPriority(String value) {
        try {
            return Integer.parseInt(value, 10);
        } catch(NumberFormatException ignored) {
            return 0;
        }
    }
    
    private boolean isValid(String line) {
        try {
            Integer.parseInt(line, 10);
            return true;
        } catch(NumberFormatException ignored) {
            return false;
        }
    }
}
