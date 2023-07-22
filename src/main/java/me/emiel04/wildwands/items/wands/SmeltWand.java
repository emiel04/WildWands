package me.emiel04.wildwands.items.wands;


import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.utils.BlockUtil;
import me.emiel04.wildwands.utils.MessageSenderUtil;
import me.emiel04.wildwands.utils.RecipeUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class SmeltWand extends Wand {
    public SmeltWand(String name, String displayName, String ukey, List<String> lore, Material material) {
        super(name, displayName, ukey, lore, material);
    }

    @Override
    public WandType getType() {
        return WandType.SMELT_WAND;
    }



    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (!(clickedBlock.getState() instanceof Container)) return;

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
        smeltAll(container.getInventory());
        playSuccess(clickedBlock);
    }

    private void playSuccess(Block block) {
        World world = block.getWorld();
        world.playSound(block.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
        double[] o = BlockUtil.getBlockParticleOffsets();
        world.spawnParticle(Particle.FLAME, BlockUtil.getBlockCenterLocation(block), 5, o[0],  o[1],  o[2], 0.01);
        world.spawnParticle(Particle.SMOKE_LARGE, BlockUtil.getBlockCenterLocation(block), 5, o[0],  o[1],  o[2], 0.01);
    }

    private void smeltAll(Inventory inventory) {
        ItemStack[] smelted = inventory.getContents();
        for (int i = 0; i < smelted.length; i++) {
            ItemStack itemStack = smelted[i];
            if (itemStack == null) continue;
            ItemStack newItemStack = RecipeUtil.getSmeltedVariantCached(itemStack);
            smelted[i] = newItemStack;
        }
        inventory.setStorageContents(smelted);
    }



}
