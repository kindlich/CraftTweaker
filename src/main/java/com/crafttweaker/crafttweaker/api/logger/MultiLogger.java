package com.crafttweaker.crafttweaker.api.logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiLogger implements ILogger {
    
    private final List<ILogger> children = new ArrayList<>();
    
    public void addLogger(ILogger logger) {
        children.add(logger);
    }
    
    public void closeChildren() throws IOException {
        for(ILogger child : children)
            if(child instanceof Closeable)
                ((Closeable) child).close();
    }
    
    @Override
    public void setLogLevel(LogLevel logLevel) {
        for(ILogger child : children) {
            child.setLogLevel(logLevel);
        }
    }
    
    @Override
    public void logInternal(@Nullable String message) {
        for(ILogger child : children) {
            child.logInternal(message);
        }
    }
    
    @Override
    public void logTrace(@Nullable String message) {
        for(ILogger child : children) {
            child.logTrace(message);
        }
    }
    
    @Override
    public void logDebug(@Nullable String message) {
        for(ILogger child : children) {
            child.logDebug(message);
        }
    }
    
    @Override
    public void logInfo(@Nullable String message) {
        for(ILogger child : children) {
            child.logInfo(message);
        }
    }
    
    @Override
    public void logCommand(@Nullable String message) {
        for(ILogger child : children) {
            child.logCommand(message);
        }
    }
    
    @Override
    public void logWarning(@Nullable String message) {
        for(ILogger child : children) {
            child.logWarning(message);
        }
    }
    
    @Override
    public void logError(@Nullable String message) {
        for(ILogger child : children) {
            child.logError(message);
        }
    }
    
    @Override
    public void logError(@Nonnull Exception e) {
        for(ILogger child : children) {
            child.logError(e);
        }
    }
    
    @Override
    public void logFatal(@Nullable String message) {
        for(ILogger child : children) {
            child.logFatal(message);
        }
    }
    
    @Override
    public void setUseTimeStamps(boolean useTimeStamps) {
        for(ILogger child : children) {
            child.setUseTimeStamps(useTimeStamps);
        }
    }
}
