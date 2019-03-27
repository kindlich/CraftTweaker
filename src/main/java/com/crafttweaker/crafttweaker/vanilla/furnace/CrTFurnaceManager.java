package com.crafttweaker.crafttweaker.vanilla.furnace;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.vanilla.furnace.internal.ActionAddFurnaceRecipe;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import net.minecraft.item.crafting.RecipeManager;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
public class CrTFurnaceManager {
    
    @ZenCodeGlobals.Global("furnace")
    public static final CrTFurnaceManager INSTANCE = new CrTFurnaceManager();
    public static RecipeManager vanillaRecipeManager;
    private final List<ActionAddFurnaceRecipe> addedRecipes = new ArrayList<>();
    
    private CrTFurnaceManager() {
    }
    
    public List<ActionAddFurnaceRecipe> getAddedRecipes() {
        return addedRecipes;
    }
    
    @ZenCodeType.Method
    public void addRecipe(String recipeName, IIngredient input, IItemStack output, float experience, int cookingTime) {
        addedRecipes.add(new ActionAddFurnaceRecipe(recipeName, input, output, experience, cookingTime));
    }
}
