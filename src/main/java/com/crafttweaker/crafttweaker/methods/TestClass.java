package com.crafttweaker.crafttweaker.methods;

import com.crafttweaker.crafttweaker.api.CraftTweakeAPI;
import com.crafttweaker.crafttweaker.api.logger.ILogger;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
public class TestClass {
    
    @ZenCodeGlobals.Global("testInstance")
    public static final TestClass TestInstance = new TestClass();
    
    @ZenCodeGlobals.Global
    public static final ILogger logger = CraftTweakeAPI.logger;
    
    TestClass() {
    }
    
    @ZenCodeGlobals.Global
    public static void println(String toPrint) {
        CraftTweakeAPI.logger.logInfo(toPrint);
    }
    
    @ZenCodeType.Method
    public void print() {
        CraftTweakeAPI.logger.logInfo("Hello");
    }
}
