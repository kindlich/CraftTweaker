package com.crafttweaker.crafttweaker.vanilla.crafting;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.vanilla.crafting.internal.ActionAddCraftingRecipe;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import net.minecraft.item.crafting.RecipeManager;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
public class CrTRecipeManager {
    
    @ZenCodeGlobals.Global("recipes")
    public static final CrTRecipeManager INSTANCE = new CrTRecipeManager();
    public static RecipeManager vanillaRecipeManager = null;
    private final List<ActionAddCraftingRecipe> addedRecipes = new ArrayList<>();
    
    private CrTRecipeManager() {
    }
    
    @ZenCodeType.Method
    public void addShaped(String recipeName, IItemStack output, IIngredient[][] ingredients, @ZenCodeType.Nullable RecipeFunctionShaped recipeFunction) {
        addedRecipes.add(new ActionAddCraftingRecipe.Shaped(recipeName, output, ingredients, true, recipeFunction));
    }
    
    @ZenCodeType.Method
    public void addShapeless(String recipeName, IItemStack output, IIngredient[] ingredients, @ZenCodeType.Nullable RecipeFunctionShapeless recipeFunction) {
        addedRecipes.add(new ActionAddCraftingRecipe.Shapeless(recipeName, output, ingredients, recipeFunction));
    }
    
    public List<ActionAddCraftingRecipe> getAddedRecipes() {
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
