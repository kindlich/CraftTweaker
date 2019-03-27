package com.crafttweaker.crafttweaker.vanilla.crafting.internal;

import com.crafttweaker.crafttweaker.api.action.AbstractAction;
import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.vanilla.crafting.CrTRecipeManager;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ActionAddCraftingRecipe extends AbstractAction {
    
    private final IRecipe recipe;
    
    ActionAddCraftingRecipe(IRecipe recipe) {
        
        this.recipe = recipe;
    }
    
    @Override
    public boolean isValid() {
        return recipe != null && CrTRecipeManager.vanillaRecipeManager != null;
    }
    
    @Nullable
    @Override
    public String describeInvalid() {
        return "Error!";
    }
    
    @Override
    public void apply() {
        if(CrTRecipeManager.vanillaRecipeManager != null)
            CrTRecipeManager.vanillaRecipeManager.addRecipe(recipe);
    }
    
    public static class Shaped extends ActionAddCraftingRecipe {
        
        private final IItemStack output;
        
        public Shaped(String recipeName, IItemStack output, IIngredient[][] ingredients, boolean mirrored, CrTRecipeManager.RecipeFunctionShaped recipeFunction) {
            super(new CrTRecipeShaped(recipeName, output, ingredients, mirrored, recipeFunction));
            this.output = output;
        }
    
        @Nonnull
        @Override
        public String describeValid() {
            return "Adding shaped recipe for " + output.toBracketString();
        }
    }
    
    public static class Shapeless extends ActionAddCraftingRecipe {
    
        private final IItemStack output;
    
        public Shapeless(String recipeName, IItemStack output, IIngredient[] ingredients, CrTRecipeManager.RecipeFunctionShapeless recipeFunction) {
            super(new CrTRecipeShapeless(recipeName, output, ingredients, recipeFunction));
            this.output = output;
        }
        
        @Nonnull
        @Override
        public String describeValid() {
            return "Adding shapeless recipe for " + output.toBracketString();
        }
    }
}
