package com.crafttweaker.crafttweaker.vanilla.furnace;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.vanilla.furnace.internal.CrTRecipeFurnace;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import net.minecraft.item.crafting.IRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
public class CrTFurnaceManager {
    
    @ZenCodeGlobals.Global("furnace")
    public static final CrTFurnaceManager INSTANCE = new CrTFurnaceManager();
    private final List<IRecipe> addedRecipes = new ArrayList<>();
    
    private CrTFurnaceManager() {
    }
    
    public List<IRecipe> getAddedRecipes() {
        return addedRecipes;
    }
    
    @ZenCodeType.Method
    public void addRecipe(String recipeName, IIngredient input, IItemStack output, float experience, int cookingTime) {
        addedRecipes.add(new CrTRecipeFurnace(recipeName, input, output, experience, cookingTime));
    }
}
