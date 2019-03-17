package com.crafttweaker.crafttweaker.zencode.preprocessors;

import com.crafttweaker.crafttweaker.zencode.ZCLoader;

import java.util.Arrays;

public class LoaderPreprocessor implements IPreprocessor {
    
    @Override
    public String getName() {
        return "loader";
    }
    
    @Override
    public boolean allowScriptToBeExecuted(ZCLoader loader, String line) {
        return Arrays.stream(line.split(" ")).anyMatch(s -> s.equalsIgnoreCase(loader.getName()));
    }
}
