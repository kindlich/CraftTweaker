package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.CraftTweaker;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileAccess {
    
    private final File baseDirectory;
    private final FileFilter filter;
    private File[] foundFiles;
    
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
}
