package com.crafttweaker.crafttweaker.main;

import org.openzen.zencode.java.*;

public class TestClass {
    
    
    @ZenCodeGlobals.Global("testInstance")
    public static final TestClass TestInstance = new TestClass();
    
    TestClass() {
    }
    
    @ZenCodeType.Method
    public void print() {
        System.out.println("Hello");
    }
    
    @ZenCodeGlobals.Global
    public static void println(String toPrint) {
        System.out.println(toPrint);
    }
    
}
