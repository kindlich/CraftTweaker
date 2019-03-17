package com.crafttweaker.crafttweaker.zencode.brackets;

import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.items.MCItemStack;
import com.crafttweaker.crafttweaker.zencode.annotations.SimpleBracketRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SimpleParsers {
    
    @SimpleBracketRegistration(name = "test", loaders = "crafttweaker")
    public static String testAbc(String in) {
        return in;
    }
    
    @Nullable
    @SimpleBracketRegistration(name = "item", loaders = "crafttweaker")
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
        if(meta != 0)
            stack.setDamage(meta);
        return new MCItemStack(stack);
    }
}
