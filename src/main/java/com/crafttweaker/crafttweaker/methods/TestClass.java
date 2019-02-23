package com.crafttweaker.crafttweaker.methods;

import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
public class TestClass {
    
    @ZenCodeGlobals.Global("testInstance")
    public static final TestClass TestInstance = new TestClass();
    
    TestClass() {
    }
    
    @ZenCodeGlobals.Global
    public static void println(String toPrint) {
        System.out.println(toPrint);
    }
    
    @ZenCodeType.Method
    public void print() {
        System.out.println("Hello");
    }
}
