package com.crafttweaker.crafttweaker;

import com.crafttweaker.crafttweaker.api.CraftTweakeAPI;
import com.crafttweaker.crafttweaker.api.logger.LogLevel;
import com.crafttweaker.crafttweaker.api.logger.PrintLogger;
import com.crafttweaker.crafttweaker.vanilla.brewing.CrTBrewingManager;
import com.crafttweaker.crafttweaker.vanilla.crafting.CrTRecipeManager;
import com.crafttweaker.crafttweaker.vanilla.crafting.internal.ActionRemoveRecipeNoIngredients;
import com.crafttweaker.crafttweaker.vanilla.furnace.CrTFurnaceManager;
import com.crafttweaker.crafttweaker.zencode.FileAccessPreprocessor;
import com.crafttweaker.crafttweaker.zencode.ZCLoader;
import com.crafttweaker.crafttweaker.zencode.preprocessors.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("crafttweaker")
public class CraftTweaker {
    
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    
    public CraftTweaker() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public static Logger getLogger() {
        return LOGGER;
    }
    
    private void setup(final FMLCommonSetupEvent event) {
    
        try {
            CraftTweakeAPI.logger.addLogger(new PrintLogger(new File("crafttweaker.log"), LogLevel.TRACE, false));
        } catch(FileNotFoundException e) {
            LOGGER.catching(Level.ERROR, e);
        }
    
        final ZCLoader zcLoader = new ZCLoader("crafttweaker");
        final File scripts = new File("scripts");
        if(!scripts.exists() && !scripts.mkdir()) {
            throw new IllegalStateException("Could not create scripts dir");
        } else if(!scripts.isDirectory()) {
            throw new IllegalStateException("Scripts dir not a dir!");
        }
        final FileAccessPreprocessor access = new FileAccessPreprocessor(scripts, p -> p.getName().endsWith(".zs"));
        
        access.addDefaultPreprocessor(new LoaderPreprocessor(), "crafttweaker");
        
        access.addPreprocessor(new ModLoadedPreprocessor());
        access.addPreprocessor(new DebugPreprocessor());
        access.addPreprocessor(new ReplacePreprocessor());
        zcLoader.execute(zcLoader.toSemantic(access));
        
        CraftTweakeAPI.logger.logInfo("HELLO FROM PREINIT");
        CraftTweakeAPI.logger.logInfo("DIRT BLOCK >> " + Blocks.DIRT.getRegistryName());
        
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }
    
    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }
    
    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        CraftTweakeAPI.logger.logInfo("HELLO from server starting");
        final RecipeManager vanillaRecipeManager = event.getServer().getRecipeManager();
        CrTRecipeManager.vanillaRecipeManager = vanillaRecipeManager;
        CrTFurnaceManager.vanillaRecipeManager = vanillaRecipeManager;
    
        CrTRecipeManager.INSTANCE.getAddedRecipes().forEach(CraftTweakeAPI::apply);
        CrTFurnaceManager.INSTANCE.getAddedRecipes().forEach(CraftTweakeAPI::apply);
        CrTBrewingManager.INSTANCE.getAddedRecipes().forEach(CraftTweakeAPI::apply);
        CraftTweakeAPI.apply(ActionRemoveRecipeNoIngredients.INSTANCE);
    }
    
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            CraftTweakeAPI.logger.logInfo("HELLO from Register Block");
        }
    }
    
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommonModEvents {
        
        private static boolean registered = false;
        
        @SubscribeEvent
        public static void onModuleCollection(@Nonnull ZCLoader.ModuleCollectionEvent event) {
            if(!registered) {
                event.addModule("crafttweaker", "crafttweaker", "com.crafttweaker.crafttweaker");
                registered = true;
            }
        }
    }
}
