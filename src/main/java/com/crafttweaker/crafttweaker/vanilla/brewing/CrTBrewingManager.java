package com.crafttweaker.crafttweaker.vanilla.brewing;

import com.crafttweaker.crafttweaker.api.action.AbstractAction;
import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.vanilla.brewing.internal.ActionAddBrewingRecipe;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
public class CrTBrewingManager {
    
    @ZenCodeGlobals.Global("brewing")
    public static final CrTBrewingManager INSTANCE = new CrTBrewingManager();
    
    private final List<ActionAddBrewingRecipe> addedRecipes = new ArrayList<>();
    
    private CrTBrewingManager() {
    }
    
    public List<ActionAddBrewingRecipe> getAddedRecipes() {
        return addedRecipes;
    }
    
    @ZenCodeType.Method
    public void addRecipe(IItemStack output, IItemStack input, IIngredient ingredient) {
        addedRecipes.add(new ActionAddBrewingRecipe(input, ingredient, output));
    }
}
