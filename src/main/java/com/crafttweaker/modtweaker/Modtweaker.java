package com.crafttweaker.modtweaker;

import com.crafttweaker.crafttweaker.zencode.CrTNativeModules;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("modtweaker")
public class Modtweaker {
    
    
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    public Modtweaker() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.HIGH, this::setup);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        CrTNativeModules.registerModule("botania", "com.crafttweaker.modtweaker.botania");
    }
}
