package com.crafttweaker.crafttweaker.api.logger;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.time.LocalTime;

public class PrintLogger implements ILogger, Closeable {
    
    private final PrintWriter writer;
    private LogLevel logLevel;
    private boolean useTimeStamps;
    
    public PrintLogger(PrintWriter writer, LogLevel logLevel, boolean useTimeStamps) {
        this.writer = writer;
        this.logLevel = logLevel;
        this.useTimeStamps = useTimeStamps;
    }
    
    public PrintLogger(File file) throws FileNotFoundException {
        this(file, LogLevel.INFO);
    }
    
    public PrintLogger(File file, LogLevel level) throws FileNotFoundException {
        this(file, level, false);
    }
    
    public PrintLogger(File file, LogLevel level, boolean useTimeStamps) throws FileNotFoundException {
        this.writer = new PrintWriter(new FileOutputStream(file, false));
        this.logLevel = level;
        this.useTimeStamps = useTimeStamps;
    }
    
    
    @Override
    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
    
    @Override
    public void logInternal(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.INTERNAL)){
            writer.println(getMessage(LogLevel.INTERNAL, message));
            writer.flush();
        }
    }
    
    @Override
    public void logTrace(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.TRACE)){
            writer.println(getMessage(LogLevel.TRACE, message));
            writer.flush();
        }
    }
    
    @Override
    public void logDebug(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.DEBUG)) {
            writer.println(getMessage(LogLevel.DEBUG, message));
            writer.flush();
        }
    }
    
    @Override
    public void logInfo(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.INFO)) {
            writer.println(getMessage(LogLevel.INFO, message));
            writer.flush();
        }
    }
    
    @Override
    public void logCommand(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.COMMAND)){
            writer.println(getMessage(LogLevel.COMMAND, message));
            writer.flush();
        }
    }
    
    @Override
    public void logWarning(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.WARNING)) {
            writer.println(getMessage(LogLevel.WARNING, message));
            writer.flush();
        }
    }
    
    @Override
    public void logError(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.ERROR)) {
            writer.println(getMessage(LogLevel.ERROR, message));
            writer.flush();
        }
    }
    
    @Override
    public void logError(@Nonnull Exception e) {
        if(this.logLevel.canLog(LogLevel.ERROR)) {
            writer.println(getMessage(LogLevel.ERROR, e.getMessage()));
            writer.flush();
        }
    }
    
    @Override
    public void logFatal(@Nullable String message) {
        if(message == null)
            return;
        if(this.logLevel.canLog(LogLevel.FATAL)) {
            writer.println(getMessage(LogLevel.FATAL, message));
            writer.flush();
        }
    }
    
    @Override
    public void setUseTimeStamps(boolean useTimeStamps) {
        this.useTimeStamps = useTimeStamps;
    }
    
    @Override
    public void close() {
        writer.close();
    }
    
    @Nonnull
    @Contract(pure = true)
    private String getMessage(@Nonnull LogLevel level, String originalMessage) {
        if(useTimeStamps)
            return String.format("[%s]%s %s", LocalTime.now(), level.getPrefix(), originalMessage);
        return String.format("%s %s", level.getPrefix(), originalMessage);
    }
    
}
