package com.blamejared.crafttweaker.impl.item;

import com.blamejared.crafttweaker.api.data.*;
import com.blamejared.crafttweaker.api.item.*;
import com.blamejared.crafttweaker.impl.data.*;
import net.minecraft.item.*;

import java.util.function.*;

public class MCItemStack extends MCItemStackAbstract {
    
    public static Supplier<MCItemStack> EMPTY = () -> new MCItemStack(ItemStack.EMPTY);
    private final ItemStack internal;
    
    public MCItemStack(ItemStack internal) {
        
        this.internal = internal;
    }
    
    @Override
    public IItemStack copy() {
        
        return new MCItemStack(internal.copy());
    }
    
    @Override
    public ItemStack getInternal() {
        
        return internal.copy();
    }
    
    @Override
    public IItemStack mutable() {
        
        return new MCItemStackMutable(internal);
    }
    
    @Override
    ItemStack getInternalReadonly() {
        
        return internal;
    }
    
    @Override
    IItemStack applyToInternal(Consumer<ItemStack> function) {
        
        final ItemStack copy = internal.copy();
        function.accept(copy);
        return new MCItemStack(copy);
    }
    
}
