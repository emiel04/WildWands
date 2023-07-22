package me.emiel04.wildwands.utils;

import me.emiel04.wildwands.WildWands;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.*;

public class BuildWandUtil {
    private static int taskId = -1;
    private static final int MAX_BLOCKS = 9;
    private static Map<UUID, Player> players = new HashMap<>();
    private static Map<UUID, BlockSelection> blockMap = new HashMap<>();

    public static void start() {

        if (taskId != -1) {
            WildWands.getInstance().getLogger().warning("Task already running!");
            return;
        }
        Bukkit.broadcastMessage("Start");
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(WildWands.getInstance(), BuildWandUtil::loop, 1, 2);
    }

    public static void registerPlayer(Player p) {
        players.put(p.getUniqueId(), p);
    }

    public static void unRegisterPlayer(Player p) {
        players.remove(p.getUniqueId());
    }

    private static int particleCounter = 0;
    private static boolean showParticles = false;


    public static void resetParticleCounter(){
        showParticles = false;
        particleCounter = 0;
    }
    public static void showParticles(){
        showParticles = true;
    }

    private static void loop() {
        if (players.isEmpty()) {
            stop();
        }
        for (Player player : players.values()) {
            BlockSelection selection = getSelections(player);
            if (selection==null)continue;
            particleCounter++;
            if (particleCounter % 2 == 0){
                showParticles();
            }

            if (showParticles){
                particlesForPlayer(player, selection.getBlocks());
                resetParticleCounter();
            }
        }
    }

    private static BlockSelection getSelections(Player player) {
        Block targetBlock = player.getTargetBlock(null, 6);
        BlockFace blockFace = BlockUtil.getBlockFace(player);
        if (blockFace == null) return null;
        Block centerBlock = targetBlock.getRelative(blockFace);
        if (!targetBlock.getType().isSolid()) return null;
        List<Block> b = getBlocksInRadius(centerBlock, targetBlock.getType(), blockFace);
        BlockSelection selection = new BlockSelection(targetBlock, centerBlock, b);

        blockMap.put(player.getUniqueId(), selection);
        return selection;
    }

    private static void particlesForPlayer(Player player, Block[] playerBlocks) {
        showBlockParticles(player, playerBlocks);
    }
    enum DIRECTION {
        UP, RIGHT, DOWN, LEFT;

        public DIRECTION getNext() {
            int nextIndex = (this.ordinal() + 1) % DIRECTION.values().length;
            return DIRECTION.values()[nextIndex];
        }
    }
    private static List<Block> getBlocksInRadius(Block centerBlock, Material material, BlockFace face) {
        List<Block> blocks = new ArrayList<>();
        List<int[]> surroundedBlocks = getSurroundedBlocks();
        int centerX = centerBlock.getX(), centerY = centerBlock.getY(), centerZ = centerBlock.getZ();
        Bukkit.broadcastMessage("fdsdsf");

        for (int[] sb : surroundedBlocks) {
            int newX = centerX + sb[0];
            int newY = centerY + sb[1];
            int newZ = centerZ + sb[2];
            Block block = centerBlock.getWorld().getBlockAt(newX, newY, newZ);
            Bukkit.broadcastMessage("=====");
            Bukkit.broadcastMessage("newX: " + newX);
            Bukkit.broadcastMessage("newY: " + newY);
            Bukkit.broadcastMessage("newZ: " + newZ);
            Bukkit.broadcastMessage("=====");
            if (block.getType().isAir()){
                blocks.add(block);
            }
        }

        return blocks;
    }

    private static List<int[]> getSurroundedBlocks(){
        List<int[]> surroundedBlocks = new ArrayList<>();
        // Some problem with absoultes
        surroundedBlocks.add(new int[]{0, -1, 0});
        surroundedBlocks.add(new int[]{0, +1, 0});
        surroundedBlocks.add(new int[]{-1, +1, 0});
        surroundedBlocks.add(new int[]{-1, +1, 0});
        surroundedBlocks.add(new int[]{-1, 0, 0});
        surroundedBlocks.add(new int[]{+1, +1, 0});
        surroundedBlocks.add(new int[]{+1, 0, 0});
        surroundedBlocks.add(new int[]{+1, -1, 0});
        return surroundedBlocks;
    }


    private static boolean isOkayBlock(Material material, Block block, BlockFace face, Block centerBlock){
        if (!block.getType().isAir()) return false;
        BlockFace oppositeFace = face.getOppositeFace();
        Block relativeBlock = block.getRelative(oppositeFace);
        if (!relativeBlock.getType().equals(material)){
            return false;
        }
        Axis axis = getAxis(face);
        Bukkit.broadcastMessage("Axis: " + axis.toString());
        if (!(hasSameAxis(block, axis, centerBlock))){
            return false;
        }

        return true;
    }

    private static boolean hasSameAxis(Block relativeBlock, Axis axis, Block block) {
        switch (axis){
            case X: {
                return relativeBlock.getLocation().getBlockX() == block.getLocation().getBlockX();
            }
            case Y: {
                return relativeBlock.getLocation().getBlockY() == block.getLocation().getBlockY();
            }
            case Z: {
                return relativeBlock.getLocation().getBlockZ() == block.getLocation().getBlockZ();
            }
            default: {
                return false;
            }
        }
    }

    public static void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
        blockMap.clear();
        taskId = -1;
    }

    private static void showBlockParticles(Player p, Block[] blocks) {
        for (Block block : blocks) {
            if (block == null) continue;
            Location loc = BlockUtil.getBlockCenterLocation(block);
            spawnParticle(p, loc);
        }
    }

    private static void spawnParticle(Player p, Location loc) {
        Particle.DustOptions options = new Particle.DustOptions(org.bukkit.Color.fromRGB(255, 0, 0), 2);
        p.spawnParticle(Particle.REDSTONE, loc, 5, options);
    }

    public static BlockSelection getPlayerBlocks(UUID uuid){
        if (!blockMap.containsKey(uuid)) return null;
        return blockMap.get(uuid);
    }

    private Map<BlockFace, BlockFace> directionMap() {
        Map<BlockFace, BlockFace> directionMap = new HashMap<>();
        directionMap.put(BlockFace.DOWN, BlockFace.WEST);
        directionMap.put(BlockFace.WEST, BlockFace.NORTH);
        directionMap.put(BlockFace.NORTH, BlockFace.EAST);
        directionMap.put(BlockFace.EAST, BlockFace.SOUTH);
        directionMap.put(BlockFace.SOUTH, BlockFace.SOUTH);
        return directionMap;
    }

    private static Axis getAxis(BlockFace face){
        switch (face){
            case SOUTH:
            case NORTH:
                return Axis.Z;
            case WEST:
            case EAST:
                return Axis.X;
            case UP:
            case DOWN:
                return Axis.Y;
            default:
                throw new IllegalArgumentException("No axis found");
        }
    }
}
