package com.crafttweaker.crafttweaker.zencode.preprocessors;

import com.crafttweaker.crafttweaker.zencode.ZCLoader;

public class DebugPreprocessor implements IPreprocessor{
    
    @Override
    public String getName() {
        return "debug";
    }
    
    @Override
    public void accept(ZCLoader loader, String line) {
    
    }
}
