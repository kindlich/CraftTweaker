package com.crafttweaker.crafttweaker.items;

import com.crafttweaker.crafttweaker.ingredient.IIngredientTransformer;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MCItemStackTransformed implements IItemStack {
    
    private final IItemStack stack;
    private final IIngredientTransformer transformer;
    
    MCItemStackTransformed(IItemStack stack, IIngredientTransformer transformer) {
        this.stack = stack;
        this.transformer = transformer;
    }
    
    @Override
    public IItemStack mutable() {
        return new MCItemStackTransformed(stack.mutable(), transformer);
    }
    
    @Override
    public IItemStack updateTag() {
        return new MCItemStackTransformed(stack.updateTag(), transformer);
    }
    
    @Override
    public IItemStack withAmount(int amount) {
        return new MCItemStackTransformed(stack.withAmount(amount), transformer);
    }
    
    @Override
    public IItemStack grow(int size) {
        return new MCItemStackTransformed(stack.grow(size), transformer);
    }
    
    @Override
    public IItemStack shrink(int size) {
        return new MCItemStackTransformed(stack.shrink(size), transformer);
    }
    
    @Override
    public ItemStack getInternal() {
        return stack.getInternal();
    }
    
    @Override
    public Ingredient asVanillaIngredient() {
        return stack.asVanillaIngredient();
    }
    
    @Override
    public String toBracketString() {
        return stack.toBracketString() + ".addTransformer({...})";
    }
    
    @Override
    public IItemStack getRemainingItem(IItemStack stack) {
        return transformer.transform(stack);
    }
}
