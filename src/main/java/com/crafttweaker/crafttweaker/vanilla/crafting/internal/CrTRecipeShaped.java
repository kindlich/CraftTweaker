package com.crafttweaker.crafttweaker.vanilla.crafting.internal;

import com.crafttweaker.crafttweaker.api.ArrayUtil;
import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.items.MCItemStack;
import com.crafttweaker.crafttweaker.items.MCMutableItemStack;
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
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CrTRecipeShaped implements IRecipe {
    
    private static final IntPair INVALID = new IntPair(-1, -1);
    
    private final IIngredient[][] ingredients;
    private final IItemStack output;
    private final boolean mirrored;
    @Nullable
    private final CrTRecipeManager.RecipeFunctionShaped function;
    private final ResourceLocation resourceLocation;
    
    public CrTRecipeShaped(String name, IItemStack output, IIngredient[][] ingredients, boolean mirrored, @Nullable CrTRecipeManager.RecipeFunctionShaped function) {
        this.resourceLocation = new ResourceLocation("crafttweaker", name);
        this.output = output;
        this.ingredients = ingredients;
        this.mirrored = mirrored;
        this.function = function;
    }
    
    private IntPair calculateOffset(IInventory inv) {
        IntPair offset = calculateOffset(this.ingredients, inv);
        if(offset != INVALID || !mirrored)
            return offset;
        final IIngredient[][] ingredients = ArrayUtil.mirror(this.ingredients);
        offset = calculateOffset(ingredients, inv);
        if(offset != INVALID)
            return offset;
        
        for(int i = 0; i < ingredients.length; i++) {
            ingredients[i] = ArrayUtil.mirror(ingredients[i]);
        }
        
        offset = calculateOffset(ingredients, inv);
        if(offset != INVALID)
            return offset;
        
        return calculateOffset(ArrayUtil.mirror(ingredients), inv);
        
    }
    
    private IntPair calculateOffset(IIngredient[][] test, IInventory inv) {
        for(int rowOffset = 0; rowOffset <= inv.getHeight() - test.length; rowOffset++) {
            offset:
            for(int columnOffset = 0; columnOffset <= inv.getWidth() - test[0].length; columnOffset++) {
                final boolean[] visited = new boolean[inv.getSizeInventory()];
                
                for(int rowIndex = 0; rowIndex < test.length; rowIndex++) {
                    final IIngredient[] row = test[rowIndex];
                    for(int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                        final IIngredient item = row[columnIndex];
                        final int slotNumber = (rowIndex + rowOffset) * inv.getWidth() + columnIndex + columnOffset;
                        final ItemStack stackInSlot = inv.getStackInSlot(slotNumber);
                        
                        if(item == null && !stackInSlot.isEmpty() || item != null && !item.matches(new MCMutableItemStack(stackInSlot))) {
                            continue offset;
                        }
                        visited[slotNumber] = true;
                    }
                }
                
                for(int i = 0; i < visited.length; i++) {
                    if(!visited[i] && !inv.getStackInSlot(i).isEmpty())
                        continue offset;
                }
                return new IntPair(rowOffset, columnOffset);
            }
        }
        return INVALID;
    }
    
    @Override
    public boolean matches(IInventory inv, @Nullable World worldIn) {
        return calculateOffset(inv) != INVALID;
    }
    
    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        final IntPair offset = calculateOffset(inv);
        if(offset == INVALID)
            return ItemStack.EMPTY;
        
        if(function == null)
            return getRecipeOutput();
        
        final int rowOffset;
        final int columnOffset;
        {
            rowOffset = offset.getX();
            columnOffset = offset.getY();
        }
        
        IItemStack[][] stacks = new IItemStack[this.ingredients.length][this.ingredients[0].length];
        for(int rowIndex = 0; rowIndex < this.ingredients.length; rowIndex++) {
            final IIngredient[] row = this.ingredients[rowIndex];
            for(int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                final IIngredient ingredient = row[columnIndex];
                if(ingredient == null)
                    continue;
                final int slotIndex = (rowIndex + rowOffset) * inv.getWidth() + columnIndex + columnOffset;
                stacks[rowIndex][columnIndex] = ingredient.getRemainingItem(new MCItemStack(inv.getStackInSlot(slotIndex)));
            }
        }
        return function.process(this.output, stacks).getInternal();
    }
    
    @Override
    public boolean canFit(int width, int height) {
        return ingredients.length >= height && ingredients[0].length >= width;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return output.getInternal();
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(IInventory inv) {
        final NonNullList<ItemStack> result = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        final int rowOffset;
        final int columnOffset;
        
        {
            final IntPair offset = calculateOffset(inv);
            rowOffset = offset.getX();
            columnOffset = offset.getY();
            
            if(offset == INVALID)
                return result;
        }
        
        for(int rowIndex = 0; rowIndex < this.ingredients.length; rowIndex++) {
            final IIngredient[] row = this.ingredients[rowIndex];
            for(int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                final IIngredient ingredient = row[columnIndex];
                if(ingredient == null)
                    continue;
                final int slotIndex = (rowIndex + rowOffset) * inv.getWidth() + columnIndex + columnOffset;
                result.set(slotIndex, ingredient.getRemainingItem(new MCItemStack(inv.getStackInSlot(slotIndex)))
                        .getInternal());
            }
        }
        
        return result;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for(IIngredient[] ingredientRow : this.ingredients)
            for(IIngredient ingredient : ingredientRow) {
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
    public IRecipeSerializer<CrTRecipeShaped> getSerializer() {
        return new SerializerStub<>(this);
    }
    
    private static final class IntPair {
        
        private final int x, y;
        
        private IntPair(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Contract(pure = true)
        int getX() {
            return x;
        }
        
        @Contract(pure = true)
        int getY() {
            return y;
        }
    }
    
}
