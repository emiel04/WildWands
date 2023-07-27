package me.emiel04.wildwands.items.wands;


import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.utils.BlockUtil;
import me.emiel04.wildwands.utils.MessageSenderUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CondenseWand extends Wand {
    public CondenseWand(String name, String displayName, String ukey, List<String> lore, Material material, boolean glow) {
        super(name, displayName, ukey, lore, material, glow);
    }

    @Override
    public WandType getType() {
        return WandType.CONDENSE_WAND;
    }


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        ItemStack item = event.getItem();
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        Player p = event.getPlayer();
        NamespacedKey idKey = getIdKey();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String ukey = pdc.get(idKey, PersistentDataType.STRING);
        if (ukey == null){
            return;
        }
        if (!ukey.equals(this.ukey)) {
            return;
        }

        event.setCancelled(true);
        if (!(clickedBlock.getState() instanceof Container)) return;

        int uses = pdc.get(getUsesKey(), PersistentDataType.INTEGER);
        if (uses == 0) {
            MessageSenderUtil.sendMessageWithPrefix(p, LangConfig.get(Lang.BROKEN_WAND));
            return;
        }
        if (uses - 1 == 0) {
            breakWand(p, item);
        }
        useWand(p, item);
        Container container = (Container) clickedBlock.getState();
        condenseAll(container.getInventory());
        playSuccess(clickedBlock);
    }

    private void condenseAll(Inventory inventory) {
        BlockIngotCalculation condensedItems = getBlocks(inventory.getContents());
        inventory.setContents(condensedItems.getInventoryContents());
        inventory.addItem(condensedItems.getBlocks());
        inventory.addItem(condensedItems.getIngots());

    }

    private void playSuccess(Block block) {
        if (block == null) return;
        World world = block.getWorld();
        world.playSound(block.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
        double[] o = BlockUtil.getBlockParticleOffsets();
        world.spawnParticle(Particle.BLOCK_CRACK, BlockUtil.getBlockCenterLocation(block), 5, o[0],  o[1],  o[2], 0.01, Material.ANVIL.createBlockData());
        world.spawnParticle(Particle.SMOKE_LARGE, BlockUtil.getBlockCenterLocation(block), 5, o[0],  o[1],  o[2], 0.01);
    }

    private BlockIngotCalculation getBlocks(ItemStack[] itemStacks) {
        Map<Material, Integer> ingotMap = new HashMap<>();
        ItemStack[] inventoryContents = new ItemStack[itemStacks.length];
        List<ItemStack> ingots = new ArrayList<>();
        List<ItemStack> blocks = new ArrayList<>();
        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack itemStack = itemStacks[i];
            if (itemStack == null) continue;
            Material itemMaterial = itemStack.getType();
            if (!blockMap.containsKey(itemMaterial)){
                inventoryContents[i] = itemStack;
                continue;
            }

            if (!ingotMap.containsKey(itemMaterial)){
                ingotMap.put(itemMaterial, 0);
            }
            int existingSize = ingotMap.get(itemMaterial);
            ingotMap.put(itemMaterial, existingSize + itemStack.getAmount());
        }

        for (Map.Entry<Material, Integer> entry: ingotMap.entrySet()) {
            Material itemMaterial = entry.getKey();
            int amount = entry.getValue();
            Material newMaterial = blockMap.get(itemMaterial);
            int remainingIngots = amount % 9;
            int blockAmount = (amount - remainingIngots) / 9;
            ingots.add(new ItemStack(itemMaterial, remainingIngots));

            while(blockAmount > 64){
                blocks.add(new ItemStack(newMaterial, 64));
                blockAmount-=64;
            }
            if (blockAmount > 0){
                blocks.add(new ItemStack(newMaterial, blockAmount));
            }
        }


        return new BlockIngotCalculation(inventoryContents, ingots, blocks);
    }


    private final Map<Material, Material> blockMap = new HashMap<Material, Material>() {{
        put(Material.IRON_INGOT, Material.IRON_BLOCK);
        put(Material.COAL, Material.COAL_BLOCK);
        put(Material.COPPER_INGOT, Material.COPPER_BLOCK);
        put(Material.GOLD_INGOT, Material.GOLD_BLOCK);
        put(Material.DIAMOND, Material.DIAMOND_BLOCK);
        put(Material.EMERALD, Material.EMERALD_BLOCK);
        put(Material.LAPIS_LAZULI, Material.LAPIS_BLOCK);
        put(Material.REDSTONE, Material.REDSTONE_BLOCK);
        put(Material.SLIME_BALL, Material.SLIME_BLOCK);
    }};

}
