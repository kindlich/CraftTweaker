package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.CraftTweaker;
import com.crafttweaker.crafttweaker.zencode.preprocessors.IPreprocessor;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.logging.log4j.Level;
import org.openzen.zencode.shared.FileSourceFile;
import org.openzen.zencode.shared.SourceFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileAccess {
    
    private final File baseDirectory;
    private final FileFilter filter;
    File[] foundFiles;
    
    public FileAccess(File baseDirectory, FileFilter filter) {
        this.foundFiles = null;
        this.filter = filter;
        if(!baseDirectory.exists())
            baseDirectory.mkdirs();
        
        this.baseDirectory = baseDirectory;
    }
    
    
    public File getBaseDirectory() {
        return baseDirectory;
    }
    
    public String getName() {
        return baseDirectory.getName();
    }
    
    public File[] getFiles() {
        return getFiles(false);
    }
    
    public File[] getFiles(boolean forceReload) {
        if(foundFiles == null || forceReload) {
            try {
                this.foundFiles = Files.walk(baseDirectory.toPath())
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .filter(filter::accept)
                        .toArray(File[]::new);
            } catch(IOException e) {
                CraftTweaker.getLogger().catching(Level.ERROR, e);
                foundFiles = new File[0];
            }
        }
        return foundFiles;
    }
    
    public SourceFile[] getSourceFiles(boolean forceReload) {
        return Arrays.stream(getFiles(forceReload))
                .map(file -> new FileSourceFile(file.getName(), file))
                .toArray(SourceFile[]::new);
    }
    
    
}
