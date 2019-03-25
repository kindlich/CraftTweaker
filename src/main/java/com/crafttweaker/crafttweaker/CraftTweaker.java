package com.crafttweaker.crafttweaker;

import com.crafttweaker.crafttweaker.vanilla.crafting.CrTRecipeManager;
import com.crafttweaker.crafttweaker.vanilla.furnace.CrTFurnaceManager;
import com.crafttweaker.crafttweaker.zencode.FileAccessPreprocessor;
import com.crafttweaker.crafttweaker.zencode.ZCLoader;
import com.crafttweaker.crafttweaker.zencode.preprocessors.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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
        
        final ZCLoader impl = new ZCLoader("crafttweaker");
        final File scripts = new File("scripts");
        if(!scripts.exists() && !scripts.mkdir()) {
            throw new IllegalStateException("Could not create scripts dir");
        } else if(!scripts.isDirectory()) {
            throw new IllegalStateException("Scripts dir not a dir!");
        }
        final FileAccessPreprocessor access = new FileAccessPreprocessor(scripts, p -> p.getName().endsWith(".zs"));
        access.addPreprocessor(new ModLoadedPreprocessor());
        access.addPreprocessor(new LoaderPreprocessor());
        access.addPreprocessor(new DebugPreprocessor());
        access.addPreprocessor(new ReplacePreprocessor());
        impl.execute(impl.toSemantic(access));
        
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
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
        LOGGER.info("HELLO from server starting");
        final RecipeManager vanillaRecipeManager = event.getServer().getRecipeManager();
        for(final IRecipe addedRecipe : CrTRecipeManager.INSTANCE.getAddedRecipes()) {
            vanillaRecipeManager.addRecipe(addedRecipe);
        }
        
        for(final IRecipe addedRecipe : CrTFurnaceManager.INSTANCE.getAddedRecipes()) {
            vanillaRecipeManager.addRecipe(addedRecipe);
        }
    }
    
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
    
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommonModEvents {
        
        private static boolean registered = false;
        
        @SubscribeEvent
        public static void onModuleCollection(@NotNull ZCLoader.ModuleCollectionEvent event) {
            if(!registered) {
                event.addModule("crafttweaker", "crafttweaker", "com.crafttweaker.crafttweaker");
                registered = true;
            }
        }
    }
}
