package com.crafttweaker.crafttweaker.main.zencode;

import com.crafttweaker.crafttweaker.api.items.IItemStack;
import com.crafttweaker.crafttweaker.api.recipes.crafting.MCRecipeManager;
import com.crafttweaker.crafttweaker.main.TestClass;
import com.crafttweaker.crafttweaker.main.ingredients.IIngredient;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.*;

public class ZenCodeInterface {
    
    @Nonnull
    @Contract(pure = true)
    public static Collection<Class<?>> collectClassesToRegister() {
        return Arrays.asList(Object.class, MCRecipeManager.class, TestClass.class, IIngredient.class, IItemStack.class);
    }
}
