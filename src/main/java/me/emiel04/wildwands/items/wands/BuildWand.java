package me.emiel04.wildwands.items.wands;


import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.utils.BlockSelection;
import me.emiel04.wildwands.utils.BlockUtil;
import me.emiel04.wildwands.utils.BuildWandUtil;
import me.emiel04.wildwands.utils.MessageSenderUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BuildWand extends Wand {
    public BuildWand(String name, String displayName, String ukey, List<String> lore, Material material) {
        super(name, displayName, ukey, lore, material);
    }

    @Override
    public WandType getType() {
        return WandType.BUILD_WAND;
    }

    @EventHandler
    public void onItemHeldEvent(PlayerItemHeldEvent event){
        Player p = event.getPlayer();
        BuildWandUtil.unRegisterPlayer(p);
        ItemStack item = p.getInventory().getItem(event.getNewSlot());
        if (item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        NamespacedKey idKey = getIdKey();
        if (!itemMeta.getPersistentDataContainer().has(idKey, PersistentDataType.STRING)) return;
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        String ukey = pdc.get(idKey, PersistentDataType.STRING);

        if (ukey == null){
            return;
        }

        if (!ukey.equals(this.ukey)) {
            return;
        }

        BuildWandUtil.registerPlayer(p);
        BuildWandUtil.start();
    };

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
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
        BlockSelection selection = BuildWandUtil.getPlayerBlocks(p.getUniqueId());
        BlockUtil.placeBlocks(selection);
    }




}
