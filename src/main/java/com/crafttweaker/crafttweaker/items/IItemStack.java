package com.crafttweaker.crafttweaker.items;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.zencode.annotations.ZenRegister;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
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
