package com.crafttweaker.crafttweaker.testHelpers;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import org.junit.Assert;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MockInventory extends InventoryBasic {
    
    private final int height;
    private final int width;
    
    
    public MockInventory(ItemStack[][] contents) {
        this(3, 3, contents);
    }
    
    public MockInventory(int height, int width, ItemStack[][] contents) {
        this(height, width);
        for(int rowNumber = 0; rowNumber < contents.length; rowNumber++) {
            final ItemStack[] row = contents[rowNumber];
            for(int columnNumber = 0; columnNumber < row.length; columnNumber++) {
                this.setInventorySlotContents(rowNumber * height + columnNumber, row[columnNumber]);
            }
        }
    }
    
    public MockInventory(int height, int width) {
        super(new TextComponentString(""), height * width);
        this.height = height;
        this.width = width;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    public WithOutput withOutput(ItemStack itemStack) {
        return new WithOutput(itemStack);
    }
    
    public WithOutput noOutput() {
        return new WithOutput(null);
    }
    
    public class WithOutput {
    
        @Nullable
        private final ItemStack itemStack;
    
        public WithOutput(@Nullable ItemStack itemStack) {
            this.itemStack = itemStack;
        }
        
        public boolean hasOutput() {
            return itemStack != null;
        }
        
        public void assertOutput(ItemStack itemStack) {
            Assert.assertSame(this.itemStack, itemStack);
        }
        
        public MockInventory getInventory() {
            return MockInventory.this;
        }
    }
}
