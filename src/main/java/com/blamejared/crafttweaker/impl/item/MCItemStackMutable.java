package com.blamejared.crafttweaker.impl.item;

import com.blamejared.crafttweaker.api.annotations.*;
import com.blamejared.crafttweaker.api.data.*;
import com.blamejared.crafttweaker.api.item.*;
import com.blamejared.crafttweaker.impl.data.*;
import com.blamejared.crafttweaker_annotations.annotations.*;
import net.minecraft.item.*;
import org.openzen.zencode.java.*;

import java.util.function.*;

/**
 * An {@link MCItemStackMutable} object is the same as any other {@link IItemStack}.
 * The only difference is that changes made to it will not create a new ItemStack, but instead modify the stack given.
 *
 * This is useful for example when you are dealing with Event Handlers and need to shrink the stack the
 * player is using without assigning a new stack.
 *
 * @docParam this <item:minecraft:dirt>.mutable()
 */
@ZenRegister
@ZenCodeType.Name("crafttweaker.api.item.MCItemStackMutable")
@Document("vanilla/api/items/MCItemStackMutable")
public class MCItemStackMutable extends MCItemStackAbstract {
    
    private final ItemStack internal;
    
    public MCItemStackMutable(ItemStack internal) {
        
        this.internal = internal;
    }
    
    @Override
    public IItemStack copy() {
        
        return new MCItemStackMutable(internal.copy());
    }
    
    @Override
    public ItemStack getInternal() {
        
        return internal;
    }
    
    @Override
    public IItemStack mutable() {
        
        return this;
    }
    
    @Override
    ItemStack getInternalReadonly() {
        
        return internal;
    }
    
    @Override
    IItemStack applyToInternal(Consumer<ItemStack> function) {
        
        function.accept(internal);
        return this;
    }
    
    @Override
    StringBuilder getBracketBase() {
        
        return super.getBracketBase().append(".mutable()");
    }
    
}
