package com.crafttweaker.crafttweaker.main.tags;


import com.crafttweaker.crafttweaker.api.tags.ITag;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;

import javax.annotation.Nonnull;

@MethodsReturnNonnullByDefault
public class MCTag implements ITag {
    
    
    private final Tag tag;
    
    public MCTag(Tag tag) {
        this.tag = tag;
    }
    
    @Override
    @Nonnull
    public Tag getInternal() {
        return tag;
    }
    
    @Override
    public Ingredient asVanillaIngredient() {
        return Ingredient.EMPTY;
    }
    
    @Override
    public String toBracketString() {
        return this.toString();
    }
}
