package com.blamejared.crafttweaker.impl.item;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.data.IData;
import com.blamejared.crafttweaker.api.data.NBTConverter;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.actions.items.ActionSetBurnTime;
import com.blamejared.crafttweaker.impl.actions.items.ActionSetFood;
import com.blamejared.crafttweaker.impl.data.MapData;
import com.blamejared.crafttweaker.impl.food.MCFood;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.NBTIngredient;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class MCItemStackAbstract implements IItemStack {
    
    abstract ItemStack getInternalReadonly();
    
    abstract IItemStack applyToInternal(Consumer<ItemStack> function);
    
    @Override
    public Ingredient asVanillaIngredient() {
        
        if(isEmpty()) {
            return Ingredient.EMPTY;
        }
        if(!hasTag()) {
            return Ingredient.fromStacks(getInternal());
        }
        return new NBTIngredient(getInternal()) {};
    }
    
    @Override
    public IItemStack[] getItems() {
        
        return new IItemStack[] {this};
    }
    
    @Override
    public ResourceLocation getRegistryName() {
        
        return getDefinition().getRegistryName();
    }
    
    @Override
    public String getOwner() {
        
        final ResourceLocation registryName = getRegistryName();
        return registryName == null ? "error" : registryName.getNamespace();
    }
    
    @Override
    public boolean isEmpty() {
        
        return getInternalReadonly().isEmpty();
    }
    
    @Override
    public int getMaxStackSize() {
        
        return getInternalReadonly().getMaxStackSize();
    }
    
    @Override
    public String getDisplayName() {
        
        return getInternalReadonly().getDisplayName().getString();
    }
    
    @Override
    public IItemStack setDisplayName(String name) {
        
        return applyToInternal(stack -> stack.setDisplayName(new StringTextComponent(name)));
    }
    
    @Override
    public IItemStack clearCustomName() {
        
        return applyToInternal(ItemStack::clearCustomName);
    }
    
    @Override
    public boolean hasDisplayName() {
        
        return getInternalReadonly().hasDisplayName();
    }
    
    @Override
    public boolean hasEffect() {
        
        return getInternalReadonly().hasEffect();
    }
    
    @Override
    public boolean isEnchantable() {
        
        return getInternalReadonly().isEnchantable();
    }
    
    @Override
    public boolean isEnchanted() {
        
        return getInternalReadonly().isEnchanted();
    }
    
    @Override
    public int getRepairCost() {
        
        return getInternalReadonly().getRepairCost();
    }
    
    @Override
    public int getAmount() {
        
        return getInternalReadonly().getCount();
    }
    
    @Override
    public IItemStack setAmount(int amount) {
        
        return applyToInternal(stack -> stack.setCount(amount));
    }
    
    @Override
    public boolean isStackable() {
        
        return getInternalReadonly().isStackable();
    }
    
    @Override
    public IItemStack withDamage(int damage) {
        
        return applyToInternal(stack -> stack.setDamage(damage));
    }
    
    @Override
    public boolean isDamageable() {
        
        return getInternalReadonly().isDamageable();
    }
    
    @Override
    public boolean isDamaged() {
        
        return getInternalReadonly().isDamaged();
    }
    
    @Override
    public int getMaxDamage() {
        
        return getInternalReadonly().getMaxDamage();
    }
    
    @Override
    public String getTranslationKey() {
        
        return getInternalReadonly().getTranslationKey();
    }
    
    @Override
    public IItemStack withTag(IData tag) {
        
        if(!(tag instanceof MapData)) {
            tag = new MapData(tag.asMap());
        }
        
        final CompoundNBT internal = ((MapData) tag).getInternal();
        return applyToInternal(stack -> stack.setTag(internal));
    }
    
    @Override
    public boolean hasTag() {
        
        return getInternalReadonly().hasTag();
    }
    
    @Override
    public IData getTag() {
        
        return NBTConverter.convert(getInternal().getTag());
    }
    
    @Override
    public IData getOrCreateTag() {
        
        return NBTConverter.convert(getInternal().getOrCreateTag());
    }
    
    @Override
    public boolean matches(IItemStack stack, boolean ignoreDamage) {
        
        ItemStack stack1 = getInternalReadonly();
        ItemStack stack2 = (stack instanceof MCItemStackAbstract) ? ((MCItemStackAbstract) stack).getInternalReadonly() : stack
                .getInternal();
        
        if(stack1.isEmpty() != stack2.isEmpty()) {
            return false;
        }
        if(stack1.getItem() != stack2.getItem()) {
            return false;
        }
        if(stack1.getCount() > stack2.getCount()) {
            return false;
        }
        // This is really just an early exit, since damage is NBT based, it is checked again in the NBT contains
        if(!ignoreDamage) {
            if(stack1.getDamage() != stack2.getDamage()) {
                return false;
            }
        }
        CompoundNBT stack1Tag = stack1.getTag();
        CompoundNBT stack2Tag = stack2.getTag();
        if(stack1Tag == null && stack2Tag == null) {
            return true;
        }
        
        // Lets just use the partial nbt
        MapData stack2Data = (MapData) NBTConverter.convert(stack2Tag);
        MapData stack1Data = (MapData) NBTConverter.convert(stack1Tag);
        if(stack1Data == null) {
            return true;
        }
        if(ignoreDamage) {
            stack1Data = (MapData) stack1Data.copyInternal();
            stack1Data.remove("Damage");
            if(stack2Data != null) {
                stack2Data = (MapData) stack2Data.copyInternal();
                stack2Data.remove("Damage");
            }
        }
        return stack2Data != null && stack2Data.contains(stack1Data);
    }
    
    @Override
    public int getUseDuration() {
        
        return getInternalReadonly().getUseDuration();
    }
    
    @Override
    public boolean isCrossbowStack() {
        
        return getInternalReadonly().isCrossbowStack();
    }
    
    @Override
    public @ZenCodeType.Nullable MCFood getFood() {
        
        final Food food = getDefinition().getFood();
        return food == null ? null : new MCFood(food);
    }
    
    @Override
    public boolean isFood() {
        
        return getInternalReadonly().isFood();
    }
    
    @Override
    public void setFood(MCFood food) {
        
        CraftTweakerAPI.apply(new ActionSetFood(this, food, getDefinition().getFood()));
    }
    
    @Override
    public int getBurnTime() {
        
        return ForgeHooks.getBurnTime(getInternalReadonly());
    }
    
    @Override
    public void setBurnTime(int time) {
        
        CraftTweakerAPI.apply(new ActionSetBurnTime(this, time));
    }
    
    @Override
    public MCWeightedItemStack percent(int percentage) {
        
        return weight(percentage / 100.0D);
    }
    
    @Override
    public MCWeightedItemStack weight(double weight) {
        
        return new MCWeightedItemStack(this, weight);
    }
    
    @Override
    public MCWeightedItemStack asWeightedItemStack() {
        
        return weight(1.00D);
    }
    
    @Override
    public Item getDefinition() {
        
        return getInternalReadonly().getItem();
    }
    
    @Override
    public int getDamage() {
        
        return getInternalReadonly().getDamage();
    }
    
    @Override
    @ZenCodeType.Operator(ZenCodeType.OperatorType.EQUALS)
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        //Implemented manually instead of using ItemStack.areItemStacksEqual to ensure it
        // stays the same as hashCode even if MC's impl would change
        final ItemStack thatStack = ((MCItemStackAbstract) o).getInternalReadonly();
        final ItemStack thisStack = this.getInternalReadonly();
        
        if(thisStack.isEmpty()) {
            return thatStack.isEmpty();
        }
        
        if(thisStack.getCount() != thatStack.getCount()) {
            return false;
        }
        
        if(!Objects.equals(thisStack.getItem(), thatStack.getItem())) {
            return false;
        }
        
        if(!Objects.equals(thisStack.getTag(), thatStack.getTag())) {
            return false;
        }
        
        return thisStack.areCapsCompatible(thatStack);
    }
    
    @Override
    public int hashCode() {
        
        final ItemStack internalReadonly = getInternalReadonly();
        return Objects.hash(internalReadonly.getCount(), internalReadonly.getItem(), internalReadonly.getTag());
    }
    
    @Override
    public String toString() {
        
        return getCommandString();
    }
    
    public String getCommandString() {
        
        final StringBuilder sb = getBracketBase();
        
        if(getInternalReadonly().getTag() != null) {
            MapData data = (MapData) NBTConverter.convert(getInternalReadonly().getTag()).copyInternal();
            //Damage is special case, if we have more special cases we can handle them here.
            if(getDefinition().isDamageable()) {
                data.remove("Damage");
            }
            if(!data.isEmpty()) {
                sb.append(".withTag(");
                sb.append(data.asString());
                sb.append(")");
            }
        }
        
        if(getDamage() > 0) {
            sb.append(".withDamage(").append(getDamage()).append(")");
        }
        
        if(getAmount() != 1) {
            sb.append(" * ").append(getAmount());
        }
        return sb.toString();
    }
    
    StringBuilder getBracketBase() {
        
        return new StringBuilder("<item:").append(getRegistryName()).append(">");
        
    }
    
}
