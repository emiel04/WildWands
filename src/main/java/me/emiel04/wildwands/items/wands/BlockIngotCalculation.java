package me.emiel04.wildwands.items.wands;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BlockIngotCalculation {
    private final ItemStack[] inventoryContents;
    private final ItemStack[]  ingots;
    private final ItemStack[] blocks;
    public BlockIngotCalculation(ItemStack[] inventoryContents, List<ItemStack> ingots, List<ItemStack> blocks) {
        this.inventoryContents = inventoryContents;
        this.ingots = ingots.toArray(new ItemStack[0]);
        this.blocks = blocks.toArray(new ItemStack[0]);
    }
    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }
    public ItemStack[] getIngots() {
        return ingots;
    }
    public ItemStack[] getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        return "BlockIngotCalculation{" +
                "inventoryContents=" + Arrays.toString(inventoryContents) +
                ", ingots=" + ingots +
                ", blocks=" + blocks +
                '}';
    }
}
