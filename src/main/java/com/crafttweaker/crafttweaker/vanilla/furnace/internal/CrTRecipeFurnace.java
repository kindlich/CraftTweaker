package com.crafttweaker.crafttweaker.vanilla.furnace.internal;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.internal.SerializerStub;
import com.crafttweaker.crafttweaker.items.IItemStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CrTRecipeFurnace extends FurnaceRecipe {
    
    private static final String recipeGroup = "";
    
    public CrTRecipeFurnace(String name, IIngredient in, IItemStack out, float exp, int time) {
        super(new ResourceLocation("crafttweaker", name), recipeGroup, in.asVanillaIngredient(), out.getInternal(), exp, time);
    }
    
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return new SerializerStub<>(this);
    }
}
