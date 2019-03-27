package com.crafttweaker.crafttweaker.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;

public class PrintLogger implements ILogger, Closeable {
    
    private final PrintWriter writer;
    private LogLevel logLevel;
    
    public PrintLogger(File file) throws IOException {
        this.writer = new PrintWriter(new FileOutputStream(file, false));
        this.logLevel = LogLevel.INFO;
    }
    
    
    @Override
    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
    
    @Override
    public void logDebug(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.DEBUG)) {
            writer.println(message);
            writer.flush();
        }
    }
    
    @Override
    public void logInfo(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.INFO)) {
            writer.println(message);
            writer.flush();
        }
    }
    
    @Override
    public void logWarning(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.WARNING)) {
            writer.println(message);
            writer.flush();
        }
    }
    
    @Override
    public void logError(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.ERROR)) {
            writer.println(message);
            writer.flush();
        }
    }
    
    @Override
    public void logError(@Nonnull Exception e) {
        if(this.logLevel.canLog(LogLevel.ERROR)) {
            writer.println(e.getMessage());
            writer.flush();
        }
    }
    
    @Override
    public void logFatal(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.FATAL)) {
            writer.println(message);
            writer.flush();
        }
    }
    
    @Override
    public void close() {
        writer.close();
    }
}
