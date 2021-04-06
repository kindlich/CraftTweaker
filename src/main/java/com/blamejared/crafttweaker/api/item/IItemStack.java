package com.blamejared.crafttweaker.api.item;


import com.blamejared.crafttweaker.api.annotations.*;
import com.blamejared.crafttweaker.api.data.*;
import com.blamejared.crafttweaker.impl.food.*;
import com.blamejared.crafttweaker.impl.item.*;
import com.blamejared.crafttweaker_annotations.annotations.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.openzen.zencode.java.*;

/**
 * This represents an item.
 * It can be retrieved using an Item BEP.
 * Is an {@link IIngredient}
 *
 * @docParam this <item:minecraft:dirt>
 */
@ZenRegister
@ZenCodeType.Name("crafttweaker.api.item.IItemStack")
@Document("vanilla/api/items/IItemStack")
@ZenWrapper(wrappedClass = "net.minecraft.item.ItemStack", displayStringFormat = "%s.getCommandString()", creationMethodFormat = "new MCItemStack(%s)", implementingClass = "com.blamejared.crafttweaker.impl.item.MCItemStack")
public interface IItemStack extends IIngredient, IIngredientWithAmount {
    
    
    /**
     * Creates a copy
     */
    @ZenCodeType.Method
    IItemStack copy();
    
    /**
     * Gets the registry name for the Item in this IItemStack
     *
     * @return registry name of the Item this IItemStack represents
     */
    @ZenCodeType.Getter("registryName")
    ResourceLocation getRegistryName();
    
    /**
     * Gets owning mod for the Item in this IItemStack
     *
     * @return owning mod of the Item this IItemStack represents
     */
    @ZenCodeType.Getter("owner")
    String getOwner();
    
    /**
     * Returns if the ItemStack is empty
     *
     * @return true if empty, false if not
     */
    @ZenCodeType.Getter("empty")
    boolean isEmpty();
    
    /**
     * Returns the max stack size of the Item in the ItemStack
     *
     * @return max stack size
     */
    @ZenCodeType.Getter("maxStackSize")
    int getMaxStackSize();
    
    
    /**
     * Gets the display name of the ItemStack
     *
     * @return formatted display name of the ItemStack.
     */
    @ZenCodeType.Getter("displayName")
    String getDisplayName();
    
    /**
     * Sets the display name of the ItemStack
     *
     * @param name New name of the stack.
     *
     * @docParam name "totally not dirt"
     */
    @ZenCodeType.Method
    IItemStack setDisplayName(String name);
    
    /**
     * Clears any custom name set for this ItemStack
     */
    @ZenCodeType.Method
    IItemStack clearCustomName();
    
    /**
     * Returns true if the ItemStack has a display name.
     *
     * @return true if a display name is present on the ItemStack.
     */
    @ZenCodeType.Getter("hasDisplayName")
    boolean hasDisplayName();
    
    /**
     * Returns true if this ItemStack has an effect.
     *
     * @return true if there is an effect.
     */
    @ZenCodeType.Getter("hasEffect")
    boolean hasEffect();
    
    /**
     * Can this ItemStack be enchanted?
     *
     * @return true if this ItemStack can be enchanted.
     */
    @ZenCodeType.Getter("isEnchantable")
    boolean isEnchantable();
    
    /**
     * Is this ItemStack enchanted?
     *
     * @return true if this ItemStack is enchanted.
     */
    @ZenCodeType.Getter("isEnchanted")
    boolean isEnchanted();
    
    /**
     * Gets the repair cost of the ItemStack, or 0 if no repair is defined.
     *
     * @return ItemStack repair cost or 0 if no repair is set.
     */
    @ZenCodeType.Getter("getRepairCost")
    int getRepairCost();
    
    /**
     * Gets the amount of Items in the ItemStack
     *
     * @return ItemStack's amount
     */
    @ZenCodeType.Getter("amount")
    int getAmount();
    
    /**
     * Sets the amount of the ItemStack
     *
     * @param amount new amount
     *
     * @docParam amount 3
     */
    @ZenCodeType.Operator(ZenCodeType.OperatorType.MUL)
    IItemStack setAmount(int amount);
    
    /**
     * Returns if the ItemStack can have an amount greater than 1
     * I.E Swords and tools are not stackable, sticks are.
     *
     * @return true if this ItemStack can contain more than one item.
     */
    @ZenCodeType.Getter("stackable")
    boolean isStackable();
    
    /**
     * Sets the damage of the ItemStack
     *
     * @param damage the new damage value
     *
     * @docParam damage 10
     */
    @ZenCodeType.Method
    IItemStack withDamage(int damage);
    
    /**
     * Returns if the ItemStack is damageable
     * I.E Swords and tools are damageable, sticks are not.
     *
     * @return true if this ItemStack can take damage.
     */
    @ZenCodeType.Getter("damageable")
    boolean isDamageable();
    
    /**
     * Returns if the ItemStack is damaged
     * I.E a Swords that is no at full durability is damaged.
     *
     * @return true if this ItemStack is damaged.
     */
    @ZenCodeType.Getter("damaged")
    boolean isDamaged();
    
    /**
     * Returns the max damage of the ItemStack
     * This is the max durability of the ItemStack.
     *
     * @return The ItemStack's max durability.
     */
    @ZenCodeType.Getter("maxDamage")
    int getMaxDamage();
    
    /**
     * Returns the unlocalized Name of the Item in the ItemStack
     *
     * @return the unlocalized name.
     */
    @ZenCodeType.Getter("translationKey")
    String getTranslationKey();
    
    /**
     * Sets the tag for the ItemStack.
     *
     * @param tag The tag to set.
     *
     * @return This itemStack if it is mutable, a new one with the changed property otherwise
     *
     * @docParam tag {Display: {lore: ["Hello"]}}
     */
    @ZenCodeType.Method
    IItemStack withTag(IData tag);
    
    
    /**
     * Returns true if this ItemStack has a Tag
     *
     * @return true if tag is present.
     */
    @ZenCodeType.Getter("hasTag")
    boolean hasTag();
    
    /**
     * Returns the NBT tag attached to this ItemStack.
     *
     * @return IData of the ItemStack NBT Tag, null if it doesn't exist.
     */
    @ZenCodeType.Getter("tag")
    IData getTag();
    
    /**
     * Returns the NBT tag attached to this ItemStack or makes a new tag.
     *
     * @return IData of the ItemStack NBT Tag, empty tag if it doesn't exist.
     */
    @ZenCodeType.Method
    @ZenCodeType.Getter("getOrCreate")
    IData getOrCreateTag();
    
    @Override
    boolean matches(IItemStack stack, boolean ignoreDamage);
    
    
    /**
     * Gets the use duration of the ItemStack
     *
     * @return use duration
     */
    @ZenCodeType.Getter("useDuration")
    int getUseDuration();
    
    /**
     * Returns true if this stack is considered a crossbow item
     *
     * @return true if stack is a crossbow
     */
    @ZenCodeType.Getter("isCrossbow")
    boolean isCrossbowStack();
    
    @ZenCodeType.Getter("food")
    @ZenCodeType.Nullable MCFood getFood();
    
    @ZenCodeType.Method
    boolean isFood();
    
    @ZenCodeType.Setter("food")
    void setFood(MCFood food);
    
    @ZenCodeType.Getter("burnTime")
    int getBurnTime();
    
    /**
     * Sets the burn time of this item, for use in the furnace and other machines
     *
     * @param time the new burn time
     *
     * @docParam time 500
     */
    @ZenCodeType.Setter("burnTime")
    void setBurnTime(int time);
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.MOD)
    MCWeightedItemStack percent(int percentage);
    
    @ZenCodeType.Method
    MCWeightedItemStack weight(double weight);
    
    @ZenCodeType.Caster(implicit = true)
    MCWeightedItemStack asWeightedItemStack();
    
    @ZenCodeType.Method
    @ZenCodeType.Getter("definition")
    @ZenCodeType.Caster(implicit = true)
    Item getDefinition();
    
    @ZenCodeType.Method
    IItemStack mutable();
    
    @ZenCodeType.Getter("damage")
    int getDamage();
    
    /**
     * Gets the internal {@link ItemStack} for this IItemStack.
     *
     * @return internal ItemStack
     */
    @ZenCodeType.Method
    @ZenCodeType.Caster(implicit = true)
    ItemStack getInternal();
    
    @ZenCodeType.Method
    @ZenCodeType.Caster(implicit = true)
    default IIngredientWithAmount asIIngredientWithAmount() {
        
        return this;
    }
    
    @Override
    default IItemStack getIngredient() {
        
        return this;
    }
    
}
