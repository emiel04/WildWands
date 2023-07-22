package me.emiel04.wildwands.items.wands;

import me.emiel04.wildwands.WildWands;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum WandType {
    SMELT_WAND("smelt-wand", "SmeltWand", "&6Smelt Wand (%uses-left%)",Arrays.asList("A wand that smelts ore in a chest"), Material.STONE_PICKAXE),
    SELL_WAND("sell-wand", "SellWand", "&6Sell Wand (%uses-left%)",Arrays.asList("A wand that sells items in a chest"), Material.GOLDEN_HORSE_ARMOR),
    BUILD_WAND("build-wand", "BuildWand", "&6Build Wand (%uses-left%)",Arrays.asList("A wand that assists you with placing multiple blocks"), Material.DEBUG_STICK),
    CONDENSE_WAND("condense-wand", "CondenseWand", "&6Condense Wand (%uses-left%)",Arrays.asList("A wand that condenses all ingots in a chest to blocks"), Material.STONE_SHOVEL);
    private final String key;
    private final String defaultName;
    private final List<String> defaultLore;
    private final Material defaultMaterial;
    private final String defaultDisplayName;

    WandType(String key, String defaultName, String defaultDisplayName,List<String> defaultLore, Material defaultMaterial) {
        this.key = key;
        this.defaultName = defaultName;
        if (defaultName.contains(" ")){
            WildWands.getInstance().getLogger().severe("A wand name cannot have a space!");
            throw new RuntimeException("A wand name cannot have a space!");
        }
        this.defaultLore = defaultLore;
        this.defaultMaterial = defaultMaterial;
        this.defaultDisplayName = defaultDisplayName;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public List<String> getDefaultLore() {
        return defaultLore;
    }

    public String getKey(){
        return this.key;
    }
    public Material getDefaultMaterial() {
        return defaultMaterial;
    }
    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }
}
