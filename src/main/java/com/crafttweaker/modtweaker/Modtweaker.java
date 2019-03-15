package com.crafttweaker.modtweaker;

import com.crafttweaker.crafttweaker.zencode.ZCLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod("modtweaker")
public class Modtweaker {
    
    
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    public Modtweaker() {
    }
    
    @Mod.EventBusSubscriber
    public static final class CommonEvents {
        
        private static boolean registered = false;
        
        @SubscribeEvent
        public static void onModuleCollect(@NotNull ZCLoader.ModuleCollectionEvent event) {
            if(!registered) {
                //TODO: Can we calculate either the package or "normal" name?
                event.addModule("mods.modtweaker.botania", "mods_botania", "com.crafttweaker.modtweaker.botania", "crafttweaker");
                registered = true;
            }
        }
    }
}
