package com.crafttweaker.crafttweaker.vanilla.crafting.internal;

import com.crafttweaker.crafttweaker.api.CraftTweakeAPI;
import com.crafttweaker.crafttweaker.api.action.AbstractAction;
import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.vanilla.crafting.CrTRecipeManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.VanillaRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ActionRemoveRecipeNoIngredients extends AbstractAction {
    
    public static final ActionRemoveRecipeNoIngredients INSTANCE = new ActionRemoveRecipeNoIngredients();
    
    private final List<IIngredient> outputs = new ArrayList<>();
    
    public void addOutput(IIngredient output) {
        outputs.add(output);
    }
    
    @Override
    public boolean isValid() {
        return !outputs.isEmpty() && CrTRecipeManager.vanillaRecipeManager != null;
    }
    
    @Nullable
    @Override
    public String describeInvalid() {
        return outputs.isEmpty() ? null : "Recipe manager not yet found!";
    }
    
    @Nonnull
    @Override
    public String describeValid() {
        return "Removing recipes for " + outputs.stream()
                .map(IIngredient::toBracketString)
                .collect(Collectors.joining(", ", "[", "]"));
    }
    
    @Override
    public void apply() {
        final Iterator<IRecipe> iterator = CrTRecipeManager.vanillaRecipeManager.getRecipes(VanillaRecipeTypes.CRAFTING)
                .iterator();
        
        while(iterator.hasNext()) {
            final IRecipe recipe = iterator.next();
            if(outputs.stream().anyMatch(ingredient -> ingredient.asVanillaIngredient().test(recipe.getRecipeOutput()))) {
                iterator.remove();
                CraftTweakeAPI.logger.logDebug("Removed recipe with id " + recipe.getId());
            }
        }
    }
}
