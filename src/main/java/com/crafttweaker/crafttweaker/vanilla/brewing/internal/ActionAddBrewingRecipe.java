package com.crafttweaker.crafttweaker.vanilla.brewing.internal;

import com.crafttweaker.crafttweaker.api.action.AbstractAction;
import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActionAddBrewingRecipe extends AbstractAction {
    
    private final BrewingRecipe recipe;
    private final IItemStack out;
    
    public ActionAddBrewingRecipe(@Nonnull IItemStack input, @Nonnull IIngredient ingredient, @Nonnull IItemStack out) {
        this.out = out;
        this.recipe = new BrewingRecipe(input.getInternal(), ingredient.asVanillaIngredient(), out.getInternal());
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Nullable
    @Override
    public String describeInvalid() {
        return "Error!";
    }
    
    @Nonnull
    @Override
    public String describeValid() {
        return "Adding brewing recipe for " + out.toBracketString();
    }
    
    @Override
    public void apply() {
        BrewingRecipeRegistry.addRecipe(recipe);
    }
}
