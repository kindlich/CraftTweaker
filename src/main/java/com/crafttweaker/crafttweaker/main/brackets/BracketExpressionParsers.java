package com.crafttweaker.crafttweaker.main.brackets;

import com.crafttweaker.crafttweaker.api.fluids.MCFluidStack;
import com.crafttweaker.crafttweaker.api.items.IItemStack;
import com.crafttweaker.crafttweaker.main.items.MCItemStack;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.openzen.zencode.java.JavaNativeModule;
import org.openzen.zenscript.codemodel.type.GlobalTypeRegistry;
import org.openzen.zenscript.parser.*;

import javax.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class BracketExpressionParsers {
    
    @Nonnull
    public static Map<String, BracketExpressionParser> collectParsers(GlobalTypeRegistry registry, JavaNativeModule module) {
        Map<String, BracketExpressionParser> out = new HashMap<>();
        try {
            out.put("item", new SimpleBracketParser(registry, module.loadStaticMethod(BracketExpressionParsers.class.getMethod("createItemStack", String.class))));
            out.put("fluid", new SimpleBracketParser(registry, module.loadStaticMethod(BracketExpressionParsers.class.getMethod("createFluidStack", String.class))));
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }
        return out;
    }
    
    
    @Nullable
    public static IItemStack createItemStack(String input) {
        final String[] split = input.split(":");
        final String stringToUse;
        final int meta;
        if(split.length <= 2) {
            stringToUse = input;
            meta = 0;
        } else {
            final int lastIndex = split.length - 1;
            stringToUse = Arrays.stream(split, 0, lastIndex).collect(Collectors.joining());
            meta = Integer.parseInt(split[lastIndex]);
            
        }
        final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stringToUse));
        if(item == null) {
            System.err.println("Could not find item matching input " + input);
            return null;
        }
        final ItemStack stack = new ItemStack(item, 1);
        stack.setDamage(meta);
        return new MCItemStack(stack);
    }
    
    @Nullable
    public static MCFluidStack createFluidStack(String input) {
        if(input.equals("null"))
            return null;
        
        return new MCFluidStack(null);
    }
}
