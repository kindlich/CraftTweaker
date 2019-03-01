package com.crafttweaker.crafttweaker.vanilla.crafting.internal;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.items.MCItemStack;
import com.crafttweaker.crafttweaker.testHelpers.MockInventory;
import com.crafttweaker.crafttweaker.testLauncher.CrTTestRunner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(CrTTestRunner.class)
public class CrTRecipeShapedTest {
    
    private static MockInventory invA = null;
    private static MockInventory invB = null;
    private static MockInventory invC = null;
    
    private static ItemStack STACK_A = null;
    private static ItemStack STACK_B = null;
    private static ItemStack STACK_C = null;
    private static ItemStack STACK_D = null;
    
    private static IItemStack I_STACK_A = null;
    private static IItemStack I_STACK_B = null;
    private static IItemStack I_STACK_C = null;
    private static IItemStack I_STACK_D = null;
    
    
    public CrTRecipeShapedTest() {
    }
    
    @BeforeClass
    public void init() {
        STACK_A = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "redstone")));
        STACK_B = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "torch")));
        STACK_C = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "iron_sword")));
        STACK_D = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "golden_sword")));
        
        I_STACK_A = new MCItemStack(STACK_A);
        I_STACK_B = new MCItemStack(STACK_B);
        I_STACK_C = new MCItemStack(STACK_C);
        I_STACK_D = new MCItemStack(STACK_D);
        
        
        invA = new MockInventory(new ItemStack[][]{{STACK_A, STACK_B}, {STACK_C, STACK_D}, {}});
        invB = new MockInventory(new ItemStack[][]{{STACK_A, STACK_B}, {}, {}});
        invC = new MockInventory(new ItemStack[][]{{STACK_C, STACK_D}, {STACK_A, STACK_B}, {}});
    }
    
    @Test
    public void matchesNoMirror() {
        final CrTRecipeShaped recipe = new CrTRecipeShaped("name_a", I_STACK_A, new IIngredient[][]{{I_STACK_A, I_STACK_B}, {I_STACK_C, I_STACK_D}}, false, null);
        Assert.assertTrue("Recipe does not match", recipe.matches(invA, null));
        Assert.assertFalse("Recipe does unexpectedly match", recipe.matches(invB, null));
        Assert.assertFalse("Mirrored setup matches even though mirrored == false", recipe.matches(invC, null));
    }
    
    @Test
    public void matchesMirror() {
        final CrTRecipeShaped recipe = new CrTRecipeShaped("name_a", I_STACK_A, new IIngredient[][]{{I_STACK_A, I_STACK_B}, {I_STACK_C, I_STACK_D}}, true, null);
        Assert.assertTrue("Recipe does not match", recipe.matches(invA, null));
        Assert.assertFalse("Recipe does unexpectedly match", recipe.matches(invB, null));
        Assert.assertTrue("Mirrored setup does not match even though mirrored == true", recipe.matches(invC, null));
    }
    
}