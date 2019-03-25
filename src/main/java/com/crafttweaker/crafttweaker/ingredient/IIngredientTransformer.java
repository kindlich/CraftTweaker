package com.crafttweaker.crafttweaker.ingredient;

import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import org.openzen.zencode.java.ZenCodeType;

@FunctionalInterface
@ZenRegister
public interface IIngredientTransformer {
    
    @ZenCodeType.Method
    IItemStack transform(IItemStack in);
}
