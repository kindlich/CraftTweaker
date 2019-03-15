package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.zencode.preprocessors.IPreprocessor;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.openzen.zencode.shared.FileSourceFile;
import org.openzen.zencode.shared.SourceFile;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileAccessPreprocessor {
    
    private final Map<String, IPreprocessor> preprocessors;
    private final Table<File, IPreprocessor, String> evaluatedFiles;
    private final FileAccess access;
    
    public FileAccessPreprocessor(File baseDirectory, FileFilter filter) {
        access = new FileAccess(baseDirectory, filter);
        preprocessors = new HashMap<>();
        evaluatedFiles = HashBasedTable.create();
    }
    
    public void addPreprocessor(IPreprocessor preprocessor) {
        preprocessors.put(preprocessor.getName(), preprocessor);
    }
    
    private void rebuildRegistry(boolean forceReload) {
        if(!forceReload && access.foundFiles != null) {
            return;
        }
        evaluatedFiles.clear();
        for(File file : access.getFiles(true)) {
            try(final BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while(reader.ready() && (line = reader.readLine().trim()).startsWith("#")) {
                    line = line.substring(1).trim();
                    final String name, value;
                    {
                        final int space = line.indexOf(' ');
                        if(space < 0) {
                            name = line;
                            value = "";
                        } else {
                            name = line.substring(0, space);
                            value = line.substring(space + 1);
                        }
                    }
                    evaluatedFiles.put(file, preprocessors.get(name), value);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Map<IPreprocessor, String> getPreprocessors(File file, boolean forceReload) {
        rebuildRegistry(forceReload);
        return this.evaluatedFiles.row(file);
    }
    
    public SourceFile[] getSourceFiles(ZCLoader engine, boolean forceReload) {
        rebuildRegistry(forceReload);
        return Arrays.stream(access.getFiles(false))
                .filter(file -> getPreprocessors(file, false).entrySet()
                        .stream()
                        .allMatch(entry -> entry.getKey().allowScriptToBeExecuted(engine, entry.getValue())))
                .map(file -> new FileSourceFile(file.getName(), file))
                .toArray(SourceFile[]::new);
    }
}
