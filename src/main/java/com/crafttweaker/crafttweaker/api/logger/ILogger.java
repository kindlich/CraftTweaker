package com.crafttweaker.crafttweaker.api.logger;

import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;

@ZenRegister
public interface ILogger extends Closeable {
    
    @ZenCodeType.Method
    void setLogLevel(LogLevel logLevel);
    
    @ZenCodeType.Method
    default void log(@Nullable String message, LogLevel level) {
        switch(level) {
            case INTERNAL:
                logInternal(message);
                break;
            case TRACE:
                logTrace(message);
                break;
            case COMMAND:
                logCommand(message);
                break;
            case DEBUG:
                logDebug(message);
                break;
            case INFO:
                logInfo(message);
                break;
            case WARNING:
                logWarning(message);
                break;
            case ERROR:
                logError(message);
                break;
            case FATAL:
                logFatal(message);
                break;
        }
    }
    
    @ZenCodeType.Method
    void logInternal(@Nullable String message);
    
    @ZenCodeType.Method
    void logTrace(@Nullable String message);
    
    @ZenCodeType.Method
    void logDebug(@Nullable String message);
    
    @ZenCodeType.Method
    void logInfo(@Nullable String message);
    
    @ZenCodeType.Method
    void logCommand(@Nullable String message);
    
    @ZenCodeType.Method
    void logWarning(@Nullable String message);
    
    @ZenCodeType.Method
    void logError(@Nullable String message);
    
    @ZenCodeType.Method
    void logError(@Nonnull Exception e);
    
    @ZenCodeType.Method
    void logFatal(@Nullable String message);
    
    @ZenCodeType.Method
    void setUseTimeStamps(boolean useTimeStamps);
    
    
}
