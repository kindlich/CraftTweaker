package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.zencode.preprocessors.IPreprocessor;
import org.jetbrains.annotations.NotNull;
import org.openzen.zencode.java.ScriptingEngine;
import org.openzen.zenscript.parser.BracketExpressionParser;
import org.openzen.zenscript.parser.PrefixedBracketParser;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CrTNativeModules {
    
    public static final String DEFAULT_MODULE_NAME = "example";
    public static final String DEFAULT_LOADER = "crafttweaker";
    public static final String DEFAULT_MODULE_BASE = "com.crafttweaker.crafttweaker";
    public static final FileAccess DEFAULT_FILE_ACCESS = new FileAccess(new File("scripts"), p -> p.getName().endsWith(".zs"));
    
    public static final PrefixedBracketParser SHARED_BEP = new PrefixedBracketParser(null);
    
    
    private static final Map<String, ScriptLoader> loaders = new HashMap<>();
    
    
    static {
        final ScriptingEngine scriptingEngine = new ScriptingEngine();
        scriptingEngine.debug = true;
        final ScriptLoader scriptLoader = new ScriptLoader(scriptingEngine, new PrefixedBracketParser(null));
        scriptLoader.registerModule(DEFAULT_MODULE_NAME, DEFAULT_MODULE_BASE);
        loaders.put(DEFAULT_LOADER, scriptLoader);
    }
    
    
    public static void registerModule(@NotNull String modid, @NotNull String basePackageName) {
        registerModule(modid, basePackageName, DEFAULT_LOADER, DEFAULT_MODULE_BASE);
    }

    
    public static void registerModule(@NotNull String modid, @NotNull String basePackageName, @NotNull String loaderName, String... dependentModules) {
        if(!loaders.containsKey(loaderName))
            throw new IllegalArgumentException(String.format("Loader %s does not exist", loaderName));
        
        final ScriptLoader scriptingEngine = loaders.get(loaderName);
        scriptingEngine.registerModule("mods_" + modid, basePackageName, dependentModules);
    }
    
    
    public static void registerClass(@NotNull Class<?> clazz) {
        for(ScriptLoader value : loaders.values()) {
            if(value.registerClass(clazz))
                return;
        }
        throw new IllegalStateException("Could not find module for class " + clazz);
    }
    
    
    public static void run(String loaderName) {
        run(loaderName, DEFAULT_FILE_ACCESS);
    }
    
    public static void run(String loaderName, FileAccess fileAccess) {
        if(!loaders.containsKey(loaderName))
            throw new IllegalArgumentException("Invalid loader name: " + loaderName);
        loaders.get(loaderName).run(fileAccess, Collections.emptyMap());
    }
    
    public static void registerPreprocessor(String loaderName, IPreprocessor preprocessor) {
        if(!loaders.containsKey(loaderName)) {
            throw new IllegalArgumentException("Invalid loaderName name: " + loaderName);
        }
        loaders.get(loaderName).registerPreprocessor(preprocessor);
    }
    
    public static void registerPreprocessorAll(IPreprocessor preprocessor) {
        ScriptLoader.sharedPreprocessors.put(preprocessor.getName(), preprocessor);
    }
    
    public static void registerBracketExpressionParser(String loaderName, String prefix, BracketExpressionParser parser) {
        if(!loaders.containsKey(loaderName)) {
            throw new IllegalArgumentException("Invalid loaderName name: " + loaderName);
        }
        loaders.get(loaderName).registerBracketExpressionParser(prefix, parser);
    }
    
    
    public static void registerBracketExpressionParserAll(String prefix, BracketExpressionParser parser) {
        SHARED_BEP.register(prefix, parser);
    }
    
    public static void registerBracketExpressionParser(String loaderName, String prefix, Method parser) {
        if(!loaders.containsKey(loaderName)) {
            throw new IllegalArgumentException("Invalid loaderName name: " + loaderName);
        }
        loaders.get(loaderName).registerBracketExpressionParser(prefix, parser);
    }
    
}
