package com.blamejared.crafttweaker.impl.item;

import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

public class MCItemStack implements IItemStack {
    
    private final ItemStack internal;
    
    public MCItemStack(ItemStack internal) {
        this.internal = internal;
    }
    
    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }
    
    @Override
    public String getDisplayName() {
        return internal.getDisplayName().getFormattedText();
    }
    
    @Override
    public int getAmount() {
        return internal.getCount();
    }
    
    @Override
    public IItemStack setAmount(int amount) {
        ItemStack newStack = internal.copy();
        newStack.setCount(amount);
        return new MCItemStack(newStack);
    }
    
    @Override
    public boolean isStackable() {
        return internal.isStackable();
    }
    
    @Override
    public boolean isDamageable() {
        return internal.isDamageable();
    }
    
    @Override
    public boolean isDamaged() {
        return internal.isDamaged();
    }
    
    @Override
    public int getMaxDamage() {
        return internal.getMaxDamage();
    }
    
    @Override
    public String getTranslationKey() {
        return internal.getTranslationKey();
    }
    
    @Override
    public String getCommandString() {
        final StringBuilder sb = new StringBuilder("<item:");
        sb.append(internal.getItem().getRegistryName());
        if(internal.getDamage() > 0)
            sb.append(":").append(internal.getDamage());
        sb.append(">");
        
        if(getAmount() != 1)
            sb.append(" * ").append(getAmount());
        return sb.toString();
    }
    
    @Override
    public ItemStack getInternal() {
        return internal;
    }
}