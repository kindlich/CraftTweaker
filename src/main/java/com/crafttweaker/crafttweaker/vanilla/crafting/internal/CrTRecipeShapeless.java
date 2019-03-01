package com.crafttweaker.crafttweaker.vanilla.crafting.internal;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.items.MCItemStack;
import com.crafttweaker.crafttweaker.vanilla.crafting.CrTRecipeManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CrTRecipeShapeless implements IRecipe {
    
    private final IIngredient[] ingredients;
    private final IItemStack output;
    @Nullable
    private final CrTRecipeManager.RecipeFunctionShapeless function;
    private final ResourceLocation resourceLocation;
    
    public CrTRecipeShapeless(String name, IItemStack output, IIngredient[] ingredients, @Nullable CrTRecipeManager.RecipeFunctionShapeless function) {
        this.resourceLocation = new ResourceLocation("crafttweaker", name);
        this.output = output;
        this.ingredients = ingredients;
        this.function = function;
    }
    
    @Override
    public boolean matches(IInventory inv, World worldIn) {
        //Don't do anything here, just make sure all slots have been visited
        final boolean[] visited = forAllUniqueMatches(inv, (ingredientIndex, matchingSlot, stack) -> { });
        
        int visitedCount = 0;
        for(int slot = 0; slot < visited.length; slot++) {
            if(visited[slot])
                visitedCount++;
            else if(!inv.getStackInSlot(slot).isEmpty())
                return false;
        }
        return visitedCount == this.ingredients.length;
    }
    
    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        if(this.function == null)
            return this.output.getInternal();
        
        final IItemStack[] stacks = new IItemStack[this.ingredients.length];
        
        forAllUniqueMatches(inv, (ingredientIndex, matchingSlot, stack) -> stacks[ingredientIndex] = stack);
        
        return this.function.process(this.output, stacks).getInternal();
    }
    
    @Override
    public boolean canFit(int width, int height) {
        return ingredients.length >= width * height;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return output.getInternal();
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(IInventory inv) {
        final NonNullList<ItemStack> remainingItems = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        forAllUniqueMatches(inv, (ingredientIndex, matchingSlot, stack) -> remainingItems.set(matchingSlot, this.ingredients[ingredientIndex]
                .getRemainingItem(stack)
                .getInternal()));
        return remainingItems;
    }
    
    /**
     * Helper method to avoid redundant code.
     * performs the Action for all matched items.
     *
     * Returns a bool array which slots have been visited, false implies the slot was not used.
     * If there are less 'true' slots than there are ingredients, then the recipe doesn't match.
     * It can be possible for a slot to not be visited but still contain items.
     * Both cases need to be checked in the matches function!
     */
    private boolean[] forAllUniqueMatches(IInventory inv, ForAllUniqueAction action) {
        final boolean[] visited = new boolean[inv.getSizeInventory()];
        
        outer:
        for(int ingredientIndex = 0; ingredientIndex < this.ingredients.length; ingredientIndex++) {
            IIngredient ingredient = this.ingredients[ingredientIndex];
            for(int i = 0; i < inv.getSizeInventory(); i++) {
                if(visited[i])
                    continue;
                
                final ItemStack stackInSlot = inv.getStackInSlot(i);
                if(stackInSlot.isEmpty())
                    continue;
                
                final MCItemStack stack = new MCItemStack(stackInSlot);
                if(ingredient.matches(stack)) {
                    visited[i] = true;
                    action.accept(ingredientIndex, i, stack);
                    continue outer;
                }
            }
        }
        return visited;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for(IIngredient ingredient : this.ingredients) {
            ingredients.add(ingredient.asVanillaIngredient());
        }
        return ingredients;
    }
    
    @Override
    public boolean isDynamic() {
        return false;
    }
    
    @Override
    public String getGroup() {
        return IRecipe.super.getGroup();
    }
    
    @Override
    public ResourceLocation getId() {
        return resourceLocation;
    }
    
    @Override
    public IRecipeSerializer<CrTRecipeShapeless> getSerializer() {
        return new SerializerStub<>(this);
    }
    
    
    private interface ForAllUniqueAction {
        
        void accept(int ingredientIndex, int matchingSlot, IItemStack stack);
        
    }
}
