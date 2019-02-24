package com.crafttweaker.crafttweaker.ingredient;

import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.items.MCItemStack;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Contract;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@ZenRegister
public interface IIngredient {
    
    @Contract(pure = true)
    Object getInternal();
    
    @Contract(pure = true)
    Ingredient asVanillaIngredient();
    
    @ZenCodeType.Method
    @Contract(pure = true)
    String toBracketString();
    
    @ZenCodeType.Method
    @Contract(pure = true)
    default boolean matches(IItemStack stack) {
        return this.asVanillaIngredient().test(stack.getInternal());
    }
    
    @ZenCodeType.Method
    default IItemStack getRemainingItem(IItemStack stack) {
        return new MCItemStack(ForgeHooks.getContainerItem(stack.getInternal()));
    }
}

