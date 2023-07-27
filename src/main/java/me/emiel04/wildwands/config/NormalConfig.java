package me.emiel04.wildwands.config;

import me.emiel04.wildwands.WildWands;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

import java.util.List;

public class NormalConfig {
    public static final WildWands plugin = WildWands.getInstance();
    public static final Configuration config = plugin.getConfig();

    public static String getName(){
        String name = config.getString("pixiedust.name");
        if (name.contains(" ")){
            throw new RuntimeException("Name cannot have a space! (" + name + ")");
        }
        return name;
    }
    public static String getDisplayName(){
        return config.getString("pixiedust.displayName");
    }
    public static List<String> getLore(){
        return config.getStringList("pixiedust.lore");
    }

    public static boolean getGlow() {
        return config.getBoolean("pixiedust.glow");
    }
    public static Material getMaterial() {
        String matStr = config.getString("pixiedust.material");
        if (matStr == null || matStr.isEmpty()) return Material.AIR;
        Material material = Material.getMaterial(matStr);
        if (material == null) return Material.AIR;
        return material;
    }
}
