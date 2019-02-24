package com.crafttweaker.crafttweaker.vanilla.crafting.internal;

import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
class SerializerStub<T extends IRecipe> implements IRecipeSerializer<T> {
    
    private final T recipe;
    
    SerializerStub(T recipe) {
        this.recipe = recipe;
    }
    
    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        return recipe;
    }
    
    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        return recipe;
    }
    
    @Override
    public void write(PacketBuffer buffer, T recipe) {
    }
    
    @Override
    public ResourceLocation getName() {
        return recipe.getId();
    }
}
