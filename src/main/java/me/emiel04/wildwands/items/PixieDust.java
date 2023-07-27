package me.emiel04.wildwands.items;

import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.items.wands.Wand;
import me.emiel04.wildwands.items.wands.WandManager;
import me.emiel04.wildwands.items.wands.WandType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class PixieDust implements Listener {
    public PixieDust(String name, String displayName, List<String> lore, Material material, boolean glow) {
        this.name = name;
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.glow = glow;
    }

    protected String name;
    protected final String displayName;
    protected final List<String> lore;
    protected final String IDENTIFIER_ID = "wildwand-pixie";
    protected final String USES_ID = "wildwand-uses";
    protected final Material material;
    protected final boolean glow;

    public ItemStack getItem(int uses) {
        ItemStack dust = new ItemStack(material);
        if (glow){
            dust.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        ItemMeta meta = dust.getItemMeta();
        if (meta == null) return new ItemStack(Material.AIR);
        meta.setUnbreakable(true);
        if (glow){
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        List<String> formattedLore = formatLore(lore, uses);
        meta.setDisplayName(formatUses(displayName, uses));
        meta.setLore(formattedLore);

        NamespacedKey usesKey = getUsesKey();
        NamespacedKey idKey = getIdKey();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(idKey, PersistentDataType.BOOLEAN, true);
        pdc.set(usesKey, PersistentDataType.INTEGER, uses);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        dust.setItemMeta(meta);

        return dust;
    }
    private String formatUses(String text, int uses) {
        String strUses = String.valueOf(uses);
        if (uses < 0) {
            strUses = "∞";
        }
        return ChatColor.translateAlternateColorCodes('&', text.replace("%uses-left%", strUses));
    }

    public NamespacedKey getIdKey() {
        return new NamespacedKey(WildWands.getInstance(), IDENTIFIER_ID);
    }

    public NamespacedKey getUsesKey() {
        return new NamespacedKey(WildWands.getInstance(), USES_ID);
    }

    protected List<String> formatLore(List<String> lore, int uses) {
        List<String> newLore = new java.util.ArrayList<>();
        for (String line :
                lore) {
            newLore.add(ChatColor.RESET + "" + ChatColor.WHITE + formatUses(line, uses));
        }
        return newLore;
    }

    public String getName() {
        return name;
    }

    @EventHandler
    public void AnvilEvent(PrepareAnvilEvent event){
        AnvilInventory inventory = event.getInventory();
        ItemStack item1 = inventory.getContents()[0];
        if (item1 == null) return;
        ItemStack item2 = inventory.getContents()[1];
        if (item2 == null) return;

        if (!item1.hasItemMeta()) return;
        if (!item2.hasItemMeta()) return;

        ItemMeta itemMeta1 = item1.getItemMeta();
        ItemMeta itemMeta2 = item2.getItemMeta();
        if (itemMeta1 == null || itemMeta2 == null) return;

        PersistentDataContainer pdc1 = itemMeta1.getPersistentDataContainer();
        PersistentDataContainer pdc2 = itemMeta2.getPersistentDataContainer();
        if (!pdc1.has(Wand.getIdKey(), PersistentDataType.STRING)) return;
        if (!pdc2.has(getIdKey(), PersistentDataType.BOOLEAN)) return;

        if (!pdc2.get(getIdKey(), PersistentDataType.BOOLEAN)) {
            return;
        }

        int pixieUses = pdc2.get(getUsesKey(), PersistentDataType.INTEGER);
        pixieUses *= item2.getAmount();

        int itemUses = pdc1.get(Wand.getUsesKey(), PersistentDataType.INTEGER);

        int newUses = itemUses < 0 ? itemUses : itemUses + pixieUses;

        pdc1.set(Wand.getUsesKey(), PersistentDataType.INTEGER, newUses);
        WandType type = WandType.findByKey(pdc1.get(Wand.getIdKey(), PersistentDataType.STRING));
        if (type == null){
            throw new RuntimeException("Non existent wand type on item");
        }
        WandManager manager = WildWands.getWandManager();
        Wand newWand = manager.get(type);
        ItemStack newItemStackForWand = newWand.getItem(newUses);
        inventory.setRepairCost(0);
        event.setResult(newItemStackForWand);
    }
}
