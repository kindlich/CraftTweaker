package com.crafttweaker.crafttweaker.main.ingredients;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.crafting.Ingredient;
import org.jetbrains.annotations.Contract;
import org.openzen.zencode.java.ZenCodeType;

@MethodsReturnNonnullByDefault
public interface IIngredient {
    
    @Contract(pure = true)
    Object getInternal();
    
    @Contract(pure = true)
    Ingredient asVanillaIngredient();
    
    @ZenCodeType.Method
    @Contract(pure = true)
    String toBracketString();
    
}
