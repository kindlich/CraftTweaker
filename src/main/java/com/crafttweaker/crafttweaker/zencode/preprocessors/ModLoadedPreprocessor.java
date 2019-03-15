package com.crafttweaker.crafttweaker.zencode.preprocessors;

import com.crafttweaker.crafttweaker.zencode.ZCLoader;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;

public class ModLoadedPreprocessor implements IPreprocessor {
    
    @Override
    public String getName() {
        return "modloaded";
    }
    
    @Override
    public void accept(ZCLoader loader, String line) {
    }
    
    @Override
    public boolean allowScriptToBeExecuted(ZCLoader loader, String line) {
        return Arrays.stream(line.split(" ")).allMatch(modid -> ModList.get().isLoaded(modid));
    }
}
