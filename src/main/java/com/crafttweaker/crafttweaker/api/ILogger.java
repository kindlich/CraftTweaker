package com.crafttweaker.crafttweaker.api;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ILogger {
    
    void setLogLevel(LogLevel logLevel);
    
    default void log(@Nullable String message, LogLevel level) {
        switch(level) {
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
    
    void logDebug(@Nullable String message);
    
    void logInfo(@Nullable String message);
    
    void logWarning(@Nullable String message);
    
    void logError(@Nullable String message);
    
    void logError(@Nonnull Exception e);
    
    void logFatal(@Nullable String message);
    
    enum LogLevel {
        DEBUG, INFO, WARNING, ERROR, FATAL;
        
        @Contract(pure = true)
        public boolean canLog(@Nonnull LogLevel messageLevel) {
            return this.ordinal() <= messageLevel.ordinal();
        }
    }
    
    
}
