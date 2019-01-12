package com.crafttweaker.crafttweaker.api.recipes.crafting;

import com.crafttweaker.crafttweaker.api.items.IItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.*;
import org.openzen.zencode.java.*;

import javax.annotation.*;
import java.util.*;

@ParametersAreNonnullByDefault
public class MCRecipeManager {
    
    public final List<IRecipe> addedRecipes = new ArrayList<>();
    
    @ZenCodeGlobals.Global("recipes")
    public static final MCRecipeManager INSTANCE = new MCRecipeManager();
    
    private MCRecipeManager() {
    }
    
    @ZenCodeType.Method
    public void addShapeless(String name, IItemStack out, IItemStack[] ins) {
        NonNullList<Ingredient> list = NonNullList.create();
        for(IItemStack in : ins) {
            if(in != null)
                list.add(in.asVanillaIngredient());
        }
        addedRecipes.add(new ShapelessRecipe(new ResourceLocation("crafttweaker", name), "", out.getInternal(), list));
    }
    
    
    @ZenCodeType.Method
    public void addShaped(String name, IItemStack out, IItemStack[][] ins) {
        NonNullList<Ingredient> list = NonNullList.create();
        
        final int height = ins.length;
        int width = 0;
        for(IItemStack[] in : ins)
            width = Math.max(width, in.length);
        
        for(IItemStack[] row : ins) {
            for(IItemStack mcItemStack : row)
                list.add(mcItemStack == null ? Ingredient.EMPTY : mcItemStack.asVanillaIngredient());
            for(int i = 0; i < width - row.length; i++)
                list.add(Ingredient.EMPTY);
        }
        
        addedRecipes.add(new ShapedRecipe(new ResourceLocation("crafttweaker", name), "", width, height, list, out.getInternal()));
    }
    
}

