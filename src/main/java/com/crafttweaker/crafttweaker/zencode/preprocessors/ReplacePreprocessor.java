package com.crafttweaker.crafttweaker.zencode.preprocessors;

import com.crafttweaker.crafttweaker.zencode.ZCLoader;
import org.openzen.zencode.shared.LiteralSourceFile;
import org.openzen.zencode.shared.SourceFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringJoiner;

public class ReplacePreprocessor implements IPreprocessor {
    
    @Override
    public String getName() {
        return "replaceContent";
    }
    
    @Override
    public SourceFile modifySourceFile(ZCLoader loader, String line, SourceFile file) {
        final String replace, replaceWith;
        {
            final String[] split = line.split(" ", 2);
            if(split.length != 2) {
                //TODO log
                return file;
            }
            replace = split[0];
            replaceWith = split[1];
        }
    
        final StringJoiner content = new StringJoiner(System.lineSeparator());
    
        try(BufferedReader reader = new BufferedReader(file.open())) {
            while(reader.ready()) {
                final String s = reader.readLine();
                content.add(s.trim().startsWith("#") ? s : s.replace(replace, replaceWith));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return new LiteralSourceFile(file.getFilename(), content.toString());
    }
}
