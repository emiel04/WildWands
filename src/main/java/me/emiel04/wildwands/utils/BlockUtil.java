package me.emiel04.wildwands.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;

public class BlockUtil {
    private BlockUtil() {
    }

    public static Location getBlockCenterLocation(Block block) {
        double x = block.getLocation().getX() + 0.5;
        double y = block.getLocation().getY() + 0.5;
        double z = block.getLocation().getZ() + 0.5;
        return new Location(block.getWorld(), x, y, z);
    }

    public static double[] getBlockParticleOffsets() {

        return new double[]{
                0.5, 0.5, 0.5
        };

    }

    public static BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }

    public static void placeBlocks(BlockSelection selection) {
        for (Block block : selection.getBlocks()) {
            if (block == null) continue;
            block.getLocation().getBlock().setType(selection.getTargetBlock().getType());
        }
    }
}
