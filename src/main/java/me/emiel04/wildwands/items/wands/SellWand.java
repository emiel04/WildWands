package me.emiel04.wildwands.items.wands;



import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.utils.BlockUtil;
import me.emiel04.wildwands.utils.ConfigUtil;
import me.emiel04.wildwands.utils.MessageSenderUtil;
import me.emiel04.wildwands.utils.EssUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.util.List;

public class SellWand extends Wand {
    public SellWand(String name, String displayName, String ukey, List<String> lore, Material material) {
        super(name, displayName, ukey, lore, material);
    }

    @Override
    public WandType getType() {
        return WandType.SELL_WAND;
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
        double value = sellAll(p, container.getInventory());
        playSuccess(clickedBlock, p, value);
    }

    private double sellAll(Player p, Inventory inventory) {
        ItemStack[] contents = inventory.getContents();
        ItemStack[] result = new ItemStack[inventory.getSize()];
        double sellValue = 0;
        Economy econ = WildWands.getEconomy();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = contents[i];
            if (itemStack==null) continue;
            BigDecimal bigPrice = EssUtil.getPrice(itemStack);
            if (bigPrice == null){
                result[i] = itemStack;
                continue;
            }
            result[i] = new ItemStack(Material.AIR);
            sellValue += bigPrice.doubleValue() * itemStack.getAmount();
        }
        inventory.setContents(result);
        econ.depositPlayer(p, sellValue);
        return sellValue;
    }



    private void playSuccess(Block block, Player player, double amount) {
        MessageSenderUtil.sendMessageWithPrefix(player, LangConfig.get(Lang.SOLD_ITEMS).replace("%amount%", String.valueOf(amount)));

        if (block == null) return;
        World world = block.getWorld();
        world.playSound(block.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1, 100);
        double[] o = BlockUtil.getBlockParticleOffsets();
        world.spawnParticle(Particle.BLOCK_DUST, BlockUtil.getBlockCenterLocation(block), 50, o[0],  o[1],  o[2], 0.01, Material.GOLD_BLOCK.createBlockData());
        world.spawnParticle(Particle.FLAME, BlockUtil.getBlockCenterLocation(block), 10, o[0],  o[1],  o[2], -0.01);
    }

}
