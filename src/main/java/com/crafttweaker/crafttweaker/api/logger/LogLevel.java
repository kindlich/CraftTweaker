package com.crafttweaker.crafttweaker.api.logger;

import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import org.jetbrains.annotations.Contract;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.Nonnull;

@ZenRegister
public enum LogLevel {
    @ZenCodeType.Field INTERNAL,
    @ZenCodeType.Field TRACE,
    @ZenCodeType.Field DEBUG,
    @ZenCodeType.Field INFO,
    @ZenCodeType.Field COMMAND,
    @ZenCodeType.Field WARNING,
    @ZenCodeType.Field ERROR,
    @ZenCodeType.Field FATAL;
    
    
    LogLevel() {
    }
    
    @Nonnull
    @ZenCodeType.Method
    @Contract(pure = true)
    public final String getPrefix() {
        return "[" + this.name() + "]";
    }
    
    @ZenCodeType.Method
    @Contract(pure = true)
    public boolean canLog(@Nonnull LogLevel messageLevel) {
        return this.ordinal() <= messageLevel.ordinal();
    }
    
}
