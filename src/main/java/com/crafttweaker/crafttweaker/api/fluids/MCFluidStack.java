package com.crafttweaker.crafttweaker.api.fluids;

import com.crafttweaker.crafttweaker.main.ingredients.IIngredient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

@MethodsReturnNonnullByDefault
public class MCFluidStack implements IIngredient {
    
    private final FluidStack stack;
    
    public MCFluidStack(FluidStack stack) {
        this.stack = stack;
    }
    
    @Override
    @Nonnull
    public FluidStack getInternal() {
        return stack;
    }
    
    @Override
    public Ingredient asVanillaIngredient() {
        return Ingredient.EMPTY;
    }
    
    @Override
    public String toBracketString() {
        return String.format("<fluid:%s>%s", this.stack.getFluid().getName(), this.stack.amount == 1 ? "" : " * " + this.stack.amount);
    }
}
