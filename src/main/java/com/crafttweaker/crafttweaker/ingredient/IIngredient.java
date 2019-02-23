package com.crafttweaker.crafttweaker.ingredient;

import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.crafting.Ingredient;
import org.jetbrains.annotations.Contract;
import org.openzen.zencode.java.ZenCodeType;

@MethodsReturnNonnullByDefault
@ZenRegister
public interface IIngredient {
    
    @Contract(pure = true)
    Object getInternal();
    
    @Contract(pure = true)
    Ingredient asVanillaIngredient();
    
    @ZenCodeType.Method
    @Contract(pure = true)
    String toBracketString();
    
}

