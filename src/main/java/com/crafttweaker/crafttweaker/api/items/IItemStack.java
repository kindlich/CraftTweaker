package com.crafttweaker.crafttweaker.api.items;

import com.crafttweaker.crafttweaker.main.ingredients.IIngredient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;


@MethodsReturnNonnullByDefault
public interface IItemStack extends IIngredient {
    
    @ZenCodeType.Method
    IItemStack mutable();
    
    @ZenCodeType.Method
    IItemStack updateTag();
    
    //@ZenCodeType.Method
    @ZenCodeType.Operator(ZenCodeType.OperatorType.MUL)
    IItemStack withAmount(int amount);
    
    @ZenCodeType.Method
    IItemStack grow(int size);
    
    @ZenCodeType.Method
    IItemStack shrink(int size);
    
    @Override
    ItemStack getInternal();
}
