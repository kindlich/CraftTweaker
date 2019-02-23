package com.crafttweaker.crafttweaker.zencode.preprocessors;

import com.crafttweaker.crafttweaker.zencode.ScriptLoader;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;

public class ModLoadedPreprocessor implements IPreprocessor {
    
    @Override
    public String getName() {
        return "modloaded";
    }
    
    @Override
    public void accept(ScriptLoader loader, String line) {
    }
    
    @Override
    public boolean allowScriptToBeExecuted(ScriptLoader loader, String line) {
        return Arrays.stream(line.split(" ")).allMatch(modid -> ModList.get().isLoaded(modid));
    }
}
