package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.api.CraftTweakeAPI;
import com.crafttweaker.crafttweaker.zencode.preprocessors.IPreprocessor;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.openzen.zencode.shared.FileSourceFile;
import org.openzen.zencode.shared.SourceFile;

import java.io.*;
import java.util.*;

public class FileAccessPreprocessor {
    
    private final Map<String, IPreprocessor> preprocessors;
    private final Map<IPreprocessor, String> defaultPreprocessors;
    private final Table<File, IPreprocessor, String> evaluatedFiles;
    private final FileAccess access;
    
    public FileAccessPreprocessor(File baseDirectory, FileFilter filter) {
        access = new FileAccess(baseDirectory, filter);
        preprocessors = new HashMap<>();
        defaultPreprocessors = new HashMap<>();
        evaluatedFiles = HashBasedTable.create();
        
        this.addDefaultPreprocessor(PriorityPreprocessor.INSTANCE, "0");
    }
    
    /**
     * Only added preprocessors will be registered to the file.
     */
    public void addPreprocessor(IPreprocessor preprocessor) {
        preprocessors.put(preprocessor.getName(), preprocessor);
    }
    
    /**
     * Default preprocessors are those that are added to every file.
     * They can be overwritten by actually specifying the preprocessors in the file.
     */
    public void addDefaultPreprocessor(IPreprocessor preprocessor, String value) {
        preprocessors.put(preprocessor.getName(), preprocessor);
        defaultPreprocessors.put(preprocessor, value);
    }
    
    private void rebuildRegistry(boolean forceReload) {
        if(!forceReload && access.foundFiles != null) {
            return;
        }
        evaluatedFiles.clear();
        for(File file : access.getFiles(true)) {
            defaultPreprocessors.forEach((preprocessor, s) -> evaluatedFiles.put(file, preprocessor, s));
            
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
                    final IPreprocessor preprocessor = preprocessors.get(name);
                    if(preprocessor != null)
                        evaluatedFiles.put(file, preprocessor, value);
                    //TODO Log warning that preprocessor was not registered to the FileAccess
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
                .sorted(new FileAccessPreprocessor.ScriptComparator())
                .map(file -> new FileSourceFile(file.getName(), file))
                .peek(file -> getPreprocessors(file.file, false).forEach((key, value) -> key.accept(engine, value)))
                .map(file -> {
                    SourceFile sourceFile = file;
                    for(Map.Entry<IPreprocessor, String> entry : getPreprocessors(file.file, false).entrySet()) {
                        sourceFile = entry.getKey().modifySourceFile(engine, entry.getValue(), file);
                    }
                    return sourceFile;
                })
                .toArray(SourceFile[]::new);
    }
    
    private final class ScriptComparator implements Comparator<File> {
        
        @Override
        public int compare(File o1, File o2) {
            return Integer.compare(getPriority(o2), getPriority(o1));
        }
        
        private int getPriority(File f) {
            final String value = getPreprocessors(f, false).get(PriorityPreprocessor.INSTANCE);
            return PriorityPreprocessor.INSTANCE.getPriority(value);
        }
    }
}
