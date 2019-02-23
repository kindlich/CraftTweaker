package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.CraftTweaker;
import com.crafttweaker.crafttweaker.zencode.preprocessors.IPreprocessor;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.openzen.zencode.java.JavaNativeModule;
import org.openzen.zencode.java.ScriptingEngine;
import org.openzen.zencode.shared.CompileException;
import org.openzen.zencode.shared.FileSourceFile;
import org.openzen.zencode.shared.SourceFile;
import org.openzen.zenscript.codemodel.FunctionParameter;
import org.openzen.zenscript.codemodel.SemanticModule;
import org.openzen.zenscript.lexer.ParseException;
import org.openzen.zenscript.parser.BracketExpressionParser;
import org.openzen.zenscript.parser.PrefixedBracketParser;
import org.openzen.zenscript.parser.SimpleBracketParser;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class ScriptLoader {
    
    static final Map<String, IPreprocessor> sharedPreprocessors = new HashMap<>();
    private final Map<String, IPreprocessor> preprocessors = new HashMap<>();
    private final ScriptingEngine scriptingEngine;
    private final PrefixedBracketParser bracketExpressionParser;
    private final Map<String, JavaNativeModule> modules = new HashMap<>();
    private boolean modulesInitialized = false;
    
    ScriptLoader(ScriptingEngine scriptingEngine, BracketExpressionParser fallbackParser) {
        this.scriptingEngine = scriptingEngine;
        this.bracketExpressionParser = new PrefixedBracketParser(fallbackParser);
    }
    
    boolean registerClass(@NotNull Class<?> clazz) {
        if(modulesInitialized)
            throw new IllegalStateException("Modules already initialized, cannot add class " + clazz);
        
        final String canonicalName = clazz.getCanonicalName();
        for(String basePackage : modules.keySet()) {
            if(canonicalName.startsWith(basePackage)) {
                registerClass(clazz, modules.get(basePackage));
                return true;
            }
        }
        return false;
    }
    
    
    private void registerClass(@NotNull Class<?> clazz, @NotNull JavaNativeModule module) {
        module.addGlobals(clazz);
        module.addClass(clazz);
    }
    
    void registerModule(String moduleName, String basePackageName) {
        if(modulesInitialized)
            throw new IllegalStateException("Modules already initialized, cannot add module " + moduleName);
        
        final JavaNativeModule nativeModule = scriptingEngine.createNativeModule(moduleName, basePackageName);
        modules.put(basePackageName, nativeModule);
    }
    
    void registerPreprocessor(IPreprocessor preprocessor) {
        this.preprocessors.put(preprocessor.getName(), preprocessor);
    }
    
    void registerBracketExpressionParser(String prefix, BracketExpressionParser parser) {
        this.bracketExpressionParser.register(prefix, parser);
    }
    
    void registerBracketExpressionParser(String prefix, Method parser) {
        final JavaNativeModule module = modules.values().stream().findAny().orElse(null);
        if(module == null)
            throw new IllegalStateException("abc");
        
        
        this.bracketExpressionParser.register(prefix, new SimpleBracketParser(scriptingEngine.registry, module.loadStaticMethod(parser)));
    }
    
    void run(FileAccess fileAccess, Map<FunctionParameter, Object> scriptParameters) {
        if(!modulesInitialized) {
            for(JavaNativeModule module : modules.values()) {
                try {
                    scriptingEngine.registerNativeProvided(module);
                } catch(CompileException e) {
                    CraftTweaker.getLogger().catching(Level.ERROR, e);
                }
            }
            modulesInitialized = true;
        }
        
        
        try {
            SemanticModule scripts = this.scriptingEngine.createScriptedModule(fileAccess.getName(), getFilesToExecute(fileAccess), bracketExpressionParser, scriptParameters
                    .keySet()
                    .toArray(new FunctionParameter[0]));
            if(!scripts.isValid()) {
                CraftTweaker.getLogger()
                        .log(Level.ERROR, fileAccess.getName() + " contains invalid scripts, it will not be loaded!");
                return;
            }
            this.scriptingEngine.registerCompiled(scripts);
            
            this.scriptingEngine.run(scriptParameters, new Loader(this.getClass().getClassLoader()));
        } catch(ParseException e) {
            CraftTweaker.getLogger().catching(Level.ERROR, e);
        }
        
        
    }
    
    private SourceFile[] getFilesToExecute(FileAccess fileAccess) {
        List<SourceFile> sourceFiles = new ArrayList<>();
        for(File file : fileAccess.getFiles()) {
            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                final String line = reader.readLine();
                if(line.contains("#")) {
                    final String preprocessorCall = line.substring(line.indexOf('#'));
                    final String preprocessorName = preprocessorCall.substring(1).split(" ", 2)[0];
                    final IPreprocessor preprocessor;
                    if(preprocessors.containsKey(preprocessorName)) {
                        preprocessor = preprocessors.get(preprocessorName);
                    } else if(sharedPreprocessors.containsKey(preprocessorName)) {
                        preprocessor = sharedPreprocessors.get(preprocessorName);
                    } else {
                        continue;
                    }
                    
                    //# + name + 1 whitespace removed and given to the preprocessor
                    if(!preprocessor.allowScriptToBeExecuted(this, preprocessorCall.substring(preprocessorName.length() + 2))) {
                        continue;
                    }
                    preprocessor.accept(this, preprocessorCall);
                    
                }
                
            } catch(IOException e) {
                e.printStackTrace();
            }
            sourceFiles.add(new FileSourceFile(file.getName(), file));
        }
        return sourceFiles.toArray(new SourceFile[0]);
    }
}
