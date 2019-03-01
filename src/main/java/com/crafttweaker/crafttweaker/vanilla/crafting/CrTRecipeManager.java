package com.crafttweaker.crafttweaker.vanilla.crafting;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.vanilla.crafting.internal.CrTRecipeShaped;
import com.crafttweaker.crafttweaker.vanilla.crafting.internal.CrTRecipeShapeless;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import net.minecraft.item.crafting.IRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
public class CrTRecipeManager {
    
    @ZenCodeGlobals.Global("recipes")
    public static final CrTRecipeManager INSTANCE = new CrTRecipeManager();
    private final List<IRecipe> addedRecipes = new ArrayList<>();
    
    private CrTRecipeManager() {
    }
    
    @ZenCodeType.Method
    public void addShaped(String recipeName, IItemStack output, IIngredient[][] ingredients, @ZenCodeType.Nullable RecipeFunctionShaped recipeFunction) {
        addedRecipes.add(new CrTRecipeShaped(recipeName, output, ingredients, true, recipeFunction));
    }
    
    @ZenCodeType.Method
    public void addShapeless(String recipeName, IItemStack output, IIngredient[] ingredients, @ZenCodeType.Nullable RecipeFunctionShapeless recipeFunction) {
        addedRecipes.add(new CrTRecipeShapeless(recipeName, output, ingredients, recipeFunction));
    }
    
    public List<IRecipe> getAddedRecipes() {
        return addedRecipes;
    }
    
    
    @FunctionalInterface
    @ZenRegister
    public interface RecipeFunctionShaped {
        
        IItemStack process(IItemStack usualOut, IItemStack[][] inputs);
    }
    
    @FunctionalInterface
    @ZenRegister
    public interface RecipeFunctionShapeless {
        
        IItemStack process(IItemStack usualOut, IItemStack[] inputs);
    }
}
