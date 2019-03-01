package com.crafttweaker.crafttweaker.testLauncher;

import cpw.mods.modlauncher.Launcher;
import net.minecraft.init.Bootstrap;

import java.util.Map;

public class CrTMCLauncher {
    
    //Fallback values
    private static final String forgeVersion = "25.0.48";
    private static final String mcpVersion = "20190213.203750";
    private static final String mcpMappings = "snapshot_20180921-1.13";
    private static final String mcVersion = "1.13.2";
    private static final String forgeGroup = "net.minecraftforge";
    
    
    public static void start() {
        
        final Map<String, String> env = System.getenv();
        
        //@formatter:off
        final String[] args = new String[] {
                "--gameDir", ".",
                "--launchTarget", "fmluserdevserver",
                "--fml.forgeVersion", env.getOrDefault("FORGE_VERSION", forgeVersion),
                "--fml.mcpVersion", env.getOrDefault("MCP_VERSION", mcpVersion),
                "--fml.mcpMappings", env.getOrDefault("MCP_MAPPINGS", mcpMappings),
                "--fml.mcVersion", env.getOrDefault("MC_VERSION", mcVersion),
                "--fml.forgeGroup", env.getOrDefault("FORGE_GROUP", forgeGroup)
        };
        //@formatter:on
        
        Launcher.main(args);
        Bootstrap.register();
    }
    
}
