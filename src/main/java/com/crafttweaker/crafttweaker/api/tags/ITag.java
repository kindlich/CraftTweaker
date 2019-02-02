package com.crafttweaker.crafttweaker.api.tags;

import com.crafttweaker.crafttweaker.main.ingredients.IIngredient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tags.Tag;

@MethodsReturnNonnullByDefault
public interface ITag extends IIngredient {
    
    @Override
    Tag getInternal();
}
