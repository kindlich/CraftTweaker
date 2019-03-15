package com.crafttweaker.crafttweaker.zencode;

import com.crafttweaker.crafttweaker.CraftTweaker;
import com.crafttweaker.crafttweaker.zencode.annotations.SimpleBracketRegistration;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import com.google.common.collect.Table;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.List;

public class ASMMagic {
    
    private ASMMagic() {
    }
    
    public static void collect(List<Class<?>> classes, Table<String, String, Method> bracketMethods) {
        for(ModFileScanData scanData : ModList.get().getAllScanData()) {
            for(ModFileScanData.AnnotationData annotation : scanData.getAnnotations()) {
                if(Type.getType(ZenRegister.class).equals(annotation.getAnnotationType())) {
                    try {
                        classes.add(annotation.getClass()
                                            .getClassLoader()
                                            .loadClass(annotation.getClassType().getClassName()));
                    } catch(ClassNotFoundException e) {
                        CraftTweaker.getLogger().catching(Level.ERROR, e);
                    }
                } else if(Type.getType(SimpleBracketRegistration.class).equals(annotation.getAnnotationType())) {
                    try {
                        final String name = (String) annotation.getAnnotationData().get("name");
                        final String loader = (String) annotation.getAnnotationData().get("loader");
                        final Method method = annotation.getClass()
                                .getClassLoader()
                                .loadClass(annotation.getClassType().getClassName())
                                .getDeclaredMethod(annotation.getMemberName().split("\\(", 2)[0], String.class);
                        bracketMethods.put(loader, name, method);
                    } catch(ClassNotFoundException | NoSuchMethodException e) {
                        CraftTweaker.getLogger().catching(Level.ERROR, e);
                    }
                }
            }
        }
    }
}
