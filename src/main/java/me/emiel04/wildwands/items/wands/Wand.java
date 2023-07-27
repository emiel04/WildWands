package me.emiel04.wildwands.items.wands;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.config.WandConfig;
import me.emiel04.wildwands.utils.MessageSenderUtil;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public abstract class Wand implements Listener {

    private String name;
    protected final String displayName;
    protected final String ukey;
    protected final List<String> lore;
    protected static final String WAND_IDENTIFIER = "wildwand-id";
    protected static final String WAND_IDENTIFIER_USES = "wildwand-uses";
    private final Material material;
    private final boolean glow;

    public Wand(String name, String displayName, String ukey, List<String> lore, Material material, boolean glow) {
        this.name = name;
        this.displayName = displayName;
        this.ukey = ukey;
        this.lore = lore;
        this.material = material;
        this.glow = glow;
    }

    public abstract WandType getType();

    public ItemStack getItem() {
        return getItem(-1);
    }

    public ItemStack getItem(int uses) {
        ItemStack smeltWand = new ItemStack(material);
        if (glow){
            smeltWand.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        ItemMeta meta = smeltWand.getItemMeta();
        if (meta == null) return new ItemStack(Material.AIR);
        if (glow){
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.setDisplayName(formatUses(displayName, uses));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);



        List<String> formattedLore = formatLore(lore, uses);
        meta.setLore(formattedLore);

        NamespacedKey usesKey = getUsesKey();
        NamespacedKey idKey = getIdKey();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(idKey, PersistentDataType.STRING, ukey);
        pdc.set(usesKey, PersistentDataType.INTEGER, uses);
        pdc.set(new NamespacedKey(WildWands.getInstance(), "random-uuid"),
                PersistentDataType.STRING,
                UUID.randomUUID().toString());

        smeltWand.setItemMeta(meta);

        return smeltWand;
    }

    protected List<String> formatLore(List<String> lore, int uses) {
        List<String> newLore = new java.util.ArrayList<>();
        for (String line :
                lore) {
            newLore.add(ChatColor.RESET + "" + ChatColor.WHITE + formatUses(line, uses));
        }
        return newLore;
    }

    private String formatUses(String text, int uses) {
        String strUses = String.valueOf(uses);
        if (uses < 0) {
            strUses = "∞";
        }
        return ChatColor.translateAlternateColorCodes('&', text.replace("%uses-left%", strUses));
    }
    protected void useWand(Player p, ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) return;
        PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(getUsesKey(), PersistentDataType.INTEGER)) return;
        int uses = pdc.get(getUsesKey(), PersistentDataType.INTEGER);
        if (uses < 0) {
            return;
        }
        uses--;
        updateWand(p, itemStack, uses);
    }

    void breakWand(Player p, ItemStack item) {
        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
        MessageSenderUtil.sendMessageWithPrefix(p, LangConfig.get(Lang.WAND_BROKE));
    }

    private void updateWand(Player p, ItemStack itemStack, int uses) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(formatUses(displayName, uses));
        meta.setLore(formatLore(lore, uses));

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(getUsesKey(), PersistentDataType.INTEGER, uses);

        itemStack.setItemMeta(meta);

        p.getInventory().setItemInMainHand(itemStack);
    }


    public String getDisplayName() {
        return displayName;
    }
    public String getDisplayName(int uses) {
        return formatUses(getDisplayName(), uses);
    }

    public List<String> getLore() {
        return lore;
    }

    public static NamespacedKey getIdKey() {
        return new NamespacedKey(WildWands.getInstance(), WAND_IDENTIFIER);
    }

    public static NamespacedKey getUsesKey() {
        return new NamespacedKey(WildWands.getInstance(), WAND_IDENTIFIER_USES);
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event){
        Player p = event.getPlayer();
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null){
            return;
        }
        if(meta.getPersistentDataContainer().has(getIdKey(), PersistentDataType.STRING)){
            event.setCancelled(true);
        }
    }

    public String getName() {
        return name;
    }

    public static Wand createWand(WandType wandType) {
        String name = WandConfig.getName(wandType);
        String displayName = WandConfig.getDisplayName(wandType);
        String key = wandType.getKey();
        List<String> lore = WandConfig.getLore(wandType);
        Material material = WandConfig.getMaterial(wandType);
        Boolean glow = WandConfig.hasGlow(wandType);

        switch (wandType) {
            case SMELT_WAND:
                return new SmeltWand(name, displayName, key, lore, material, glow);
            case CONDENSE_WAND:
                return new CondenseWand(name, displayName, key, lore, material, glow);
            case SELL_WAND:
                return new SellWand(name, displayName, key, lore, material, glow);
            case BUILD_WAND:
                return new BuildWand(name, displayName, key, lore, material, glow);
            default:
                throw new IllegalArgumentException("Invalid wand type: " + wandType);
        }
    }


    @Override
    public String toString() {
        return "Wand{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", ukey='" + ukey + '\'' +
                ", lore=" + lore +
                ", WAND_IDENTIFIER='" + WAND_IDENTIFIER + '\'' +
                ", WAND_IDENTIFIER_USES='" + WAND_IDENTIFIER_USES + '\'' +
                ", material=" + material +
                '}';
    }
}
