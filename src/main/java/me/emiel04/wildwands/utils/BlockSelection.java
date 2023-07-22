package me.emiel04.wildwands.utils;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.List;

public class BlockSelection {
    private final Block targetBlock;
    private final Block centerBlock;
    public final int MAX_SIZE = 9;
    private final Block[] blocks = new Block[MAX_SIZE];

    public BlockSelection(Block targetBlock, Block centerBlock) {
        this.targetBlock = targetBlock;
        this.centerBlock = centerBlock;
    }
    public BlockSelection(Block targetBlock, Block centerBlock, List<Block> blockList) {
        this.targetBlock = targetBlock;
        this.centerBlock = centerBlock;
        addBlocks(blockList);
    }
    public void addBlocks(List<Block> blockList){
        int i = 1;
        blocks[0] = centerBlock;
        while (i < MAX_SIZE && i < blockList.size()){
            blocks[i] = blockList.get(i);
            i++;
        }

    }

    public Block getTargetBlock() {
        return targetBlock;
    }

    public Block getCenterBlock() {
        return centerBlock;
    }

    public Block[] getBlocks() {
        return blocks;
    }
}
