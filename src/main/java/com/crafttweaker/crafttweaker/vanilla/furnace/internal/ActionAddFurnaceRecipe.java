package com.crafttweaker.crafttweaker.vanilla.furnace.internal;

import com.crafttweaker.crafttweaker.api.action.AbstractAction;
import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.vanilla.furnace.CrTFurnaceManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActionAddFurnaceRecipe extends AbstractAction {
    
    private final CrTRecipeFurnace recipeFurnace;
    private final IItemStack out;
    
    public ActionAddFurnaceRecipe(String name, IIngredient ingredient, IItemStack out, float exp, int time) {
        this.out = out;
        recipeFurnace = new CrTRecipeFurnace(name, ingredient, out, exp, time);
    }
    
    @Override
    public boolean isValid() {
        return recipeFurnace != null && CrTFurnaceManager.vanillaRecipeManager != null;
    }
    
    @Nullable
    @Override
    public String describeInvalid() {
        return "Error!";
    }
    
    @Nonnull
    @Override
    public String describeValid() {
        return "Adding furnace recipe for " + out.toBracketString();
    }
    
    @Override
    public void apply() {
        if(CrTFurnaceManager.vanillaRecipeManager != null) {
            CrTFurnaceManager.vanillaRecipeManager.addRecipe(recipeFurnace);
        }
    }
}
