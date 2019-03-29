package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.api.CraftTweakeAPI;
import com.crafttweaker.crafttweaker.api.logger.ILogger;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.openzen.zencode.java.JavaNativeLoader;
import org.openzen.zencode.java.JavaNativeModule;
import org.openzen.zencode.java.ScriptingEngine;
import org.openzen.zencode.shared.CompileException;
import org.openzen.zencode.shared.SourceFile;
import org.openzen.zenscript.codemodel.FunctionParameter;
import org.openzen.zenscript.codemodel.SemanticModule;
import org.openzen.zenscript.codemodel.definition.ZSPackage;
import org.openzen.zenscript.codemodel.member.ref.FunctionalMemberRef;
import org.openzen.zenscript.lexer.ParseException;
import org.openzen.zenscript.parser.BracketExpressionParser;
import org.openzen.zenscript.parser.PrefixedBracketParser;
import org.openzen.zenscript.parser.SimpleBracketParser;
import org.openzen.zenscript.validator.ValidationLogEntry;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ZCLoader {
    
    private static final List<Class<?>> classes = new ArrayList<>();
    private static final Table<String, String, Method> bracketMethods = HashBasedTable.create();
    private static boolean collected = false;
    
    private final ScriptingEngine engine;
    private final String name;
    private final JavaNativeModule BEPModule;
    
    public ZCLoader(String name) {
        this.name = name;
        collect();
        
        final ZSPackage root = ZSPackage.createRoot();
        final JavaNativeLoader loader;
        {
            final Class[] classArray = classes.toArray(new Class[]{});
            loader = new JavaNativeLoader(classArray, classArray);
        }
        
        MinecraftForge.EVENT_BUS.post(new ModuleCollectionEvent(loader, root));
        
        try {
            engine = loader.load();
            final JavaNativeModule[] allModules = engine.getNativeModules().toArray(new JavaNativeModule[]{});
            BEPModule = engine.createNativeModule("bracket_parsers", "", allModules);
            engine.registerNativeProvided(BEPModule);
        } catch(CompileException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static void collect() {
        if(!collected) {
            ASMMagic.collect(classes, bracketMethods);
            collected = true;
        }
    }
    
    @Nullable
    public SemanticModule toSemantic(@Nonnull FileAccess access) {
        try {
            return engine.createScriptedModule(name, access.getSourceFiles(false), getParser(), new FunctionParameter[]{});
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Nullable
    public SemanticModule toSemantic(@Nonnull FileAccessPreprocessor access) {
        try {
            final SourceFile[] sourceFiles = access.getSourceFiles(this, false);
            return engine.createScriptedModule(name, sourceFiles, getParser(), new FunctionParameter[]{}, s -> CraftTweakeAPI.logger
                    .logError(s.toString()), v -> CraftTweakeAPI.logger.log(v.position + ": " + v.message, v.kind == ValidationLogEntry.Kind.ERROR ? ILogger.LogLevel.ERROR : ILogger.LogLevel.WARNING), s -> CraftTweakeAPI.logger.logDebug("Parsing " + s.getFilename())
            );
        } catch(ParseException e) {
            e.printStackTrace();
        } return null;
    }
    
    public void execute(SemanticModule module) {
        if(!module.isValid()) {
            CraftTweakeAPI.logger.logError("Module Invalid, won't execute!");
            return;
        }
        engine.registerCompiled(module);
        engine.run(Collections.emptyMap(), new Loader(this.getClass().getClassLoader()));
    }
    
    @Nonnull
    @Contract(value = " -> new", pure = true)
    private BracketExpressionParser getParser() {
        final PrefixedBracketParser prefixedBracketParser = new PrefixedBracketParser(null);
        
        Stream.concat(bracketMethods.row(name).entrySet().stream(), bracketMethods.row("").entrySet().stream())
                .forEach(entry -> {
                    final FunctionalMemberRef method = BEPModule.loadStaticMethod(entry.getValue());
                    final SimpleBracketParser parser = new SimpleBracketParser(engine.registry, method);
                    prefixedBracketParser.register(entry.getKey(), parser);
                });
        
        return prefixedBracketParser;
    }
    
    public String getName() {
        return name;
    }
    
    public static final class ModuleCollectionEvent extends Event {
        
        public final List<JavaNativeLoader.LoadingModule> registeredModules;
        private final JavaNativeLoader loader;
        private final ZSPackage root;
        
        ModuleCollectionEvent(JavaNativeLoader loader, ZSPackage root) {
            this.loader = loader;
            this.root = root;
            registeredModules = new ArrayList<>();
        }
        
        public JavaNativeLoader.LoadingModule addModule(String zsPackageName, String name, String basePackage, String... dependencies) {
            final JavaNativeLoader.LoadingModule module = loader.addModule(root.getRecursive(zsPackageName), name, basePackage, dependencies);
            registeredModules.add(module);
            return module;
        }
    }
}
