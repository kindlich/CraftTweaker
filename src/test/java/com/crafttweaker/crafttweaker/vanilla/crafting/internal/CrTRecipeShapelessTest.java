package com.crafttweaker.crafttweaker.vanilla.crafting.internal;

import com.crafttweaker.crafttweaker.ingredient.IIngredient;
import com.crafttweaker.crafttweaker.items.IItemStack;
import com.crafttweaker.crafttweaker.items.MCItemStack;
import com.crafttweaker.crafttweaker.testHelpers.MockInventory;
import com.crafttweaker.crafttweaker.testLauncher.CrTTestRunner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(CrTTestRunner.class)
public class CrTRecipeShapelessTest {
    
    private static MockInventory invA = null;
    private static MockInventory invB = null;
    private static MockInventory invC = null;
    private static MockInventory invD = null;
    
    private static ItemStack STACK_A = null;
    private static ItemStack STACK_B = null;
    private static ItemStack STACK_C = null;
    private static ItemStack STACK_D = null;
    
    private static IItemStack I_STACK_A = null;
    private static IItemStack I_STACK_B = null;
    private static IItemStack I_STACK_C = null;
    private static IItemStack I_STACK_D = null;
    
    
    public CrTRecipeShapelessTest() {
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
        invD = new MockInventory(new ItemStack[][]{{STACK_A, STACK_B}, {STACK_C, STACK_D}, {STACK_A}});
    }
    
    @Test
    public void matchesNoMirror() {
        final CrTRecipeShapeless recipe = new CrTRecipeShapeless("name_a", I_STACK_A, new IIngredient[]{I_STACK_A, I_STACK_B, I_STACK_C, I_STACK_D}, null);
        Assert.assertTrue("Recipe does not match", recipe.matches(invA, null));
        Assert.assertFalse("Recipe does unexpectedly match", recipe.matches(invB, null));
        Assert.assertTrue("Recipe in different order does not match", recipe.matches(invC, null));
    }
    
    
    
    public void matchesAdditionalItems() {
        final CrTRecipeShapeless recipe = new CrTRecipeShapeless("name_a", I_STACK_A, new IIngredient[]{I_STACK_A, I_STACK_B, I_STACK_C, I_STACK_D}, null);
        Assert.assertFalse("Recipe matching with additional items present", recipe.matches(invD, null));
    }
    
    @Test
    public void matchesOutput_NoFunction() {
        final CrTRecipeShapeless recipe = new CrTRecipeShapeless("name_a", I_STACK_A, new IIngredient[]{I_STACK_A, I_STACK_B, I_STACK_C, I_STACK_D}, null);
        Assert.assertTrue("Recipe returns incorrect output", ItemStack.areItemsEqual(STACK_A, recipe.getCraftingResult(invA)));
        Assert.assertFalse("Recipe output is empty", recipe.getCraftingResult(invA).isEmpty());
        
        Assert.assertTrue("Recipe returns incorrect output", ItemStack.areItemsEqual(STACK_A, recipe.getCraftingResult(invC)));
        Assert.assertFalse("Recipe output is empty", recipe.getCraftingResult(invC).isEmpty());
    }
    
    @Test
    public void matchesOutput_Function() {
        final CrTRecipeShapeless recipe = new CrTRecipeShapeless("name_a", I_STACK_A, new IIngredient[]{I_STACK_A, I_STACK_B, I_STACK_C, I_STACK_D}, (usualOut, inputs) -> usualOut);
        Assert.assertTrue("Recipe returns invalid output", ItemStack.areItemStacksEqual(recipe.getCraftingResult(invA), STACK_A));
    }
    
    @Test
    public void matchesOutput_Function2() {
        final CrTRecipeShapeless recipe = new CrTRecipeShapeless("name_a", I_STACK_A, new IIngredient[]{I_STACK_A, I_STACK_B, I_STACK_C, I_STACK_D}, (usualOut, inputs) -> I_STACK_D);
        Assert.assertTrue("Recipe returns invalid output", ItemStack.areItemStacksEqual(recipe.getCraftingResult(invA), STACK_D));
    }
    
    
    @Test
    public void getRemainingItems() {
        final CrTRecipeShapeless recipe = new CrTRecipeShapeless("name_a", I_STACK_A, new IIngredient[]{I_STACK_A, I_STACK_B, I_STACK_C, I_STACK_D}, null);
        for(ItemStack remainingItem : recipe.getRemainingItems(invA)) {
            Assert.assertTrue("Default remaining item is not empty", remainingItem.isEmpty());
        }
    }
    
    @Test
    public void getRemainingItems_Transformer() {
        final IItemStack A_Transformed = new MCItemStack(STACK_A) {
            @Override
            public IItemStack getRemainingItem(IItemStack stack) {
                return I_STACK_D;
            }
        };
        final MockInventory inv = new MockInventory(new ItemStack[][]{{STACK_A, STACK_A, STACK_B}, {STACK_C}, {}});
        
        final CrTRecipeShapeless recipe = new CrTRecipeShapeless("name_a", I_STACK_A, new IIngredient[]{A_Transformed, I_STACK_B, I_STACK_C, A_Transformed}, null);
    
        final NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(inv);
        Assert.assertTrue("Transformed Item not returned", ItemStack.areItemStacksEqual(STACK_D, remainingItems.get(0)));
        Assert.assertTrue("Transformed Item not returned", ItemStack.areItemStacksEqual(STACK_D, remainingItems.get(1)));
        
    
    }
    
    @Test
    public void getRecipeId() {
        final IIngredient[] ingredients = new IIngredient[]{I_STACK_A, I_STACK_B, I_STACK_C, I_STACK_D};
        CrTRecipeShapeless recipe = new CrTRecipeShapeless("name_a", I_STACK_A, ingredients, null);
        Assert.assertEquals("Invalid generated recipeID", new ResourceLocation("crafttweaker", "name_a"), recipe.getId());
        
        recipe = new CrTRecipeShapeless("name", I_STACK_A, ingredients, null);
        Assert.assertEquals("Invalid generated recipeID", new ResourceLocation("crafttweaker", "name"), recipe.getId());
    
        recipe = new CrTRecipeShapeless("some_other_name", I_STACK_A, ingredients, null);
        Assert.assertEquals("Invalid generated recipeID", new ResourceLocation("crafttweaker", "some_other_name"), recipe.getId());
    }
    
}