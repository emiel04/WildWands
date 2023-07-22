package me.emiel04.wildwands.config;

import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.items.wands.WandType;
import me.emiel04.wildwands.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WandConfig {
    private static File file;
    private static FileConfiguration configuration;
    private static WildWands plugin;
    public static void init(WildWands plugin){
        file = new File(plugin.getDataFolder(), "wands.yml");
        WandConfig.plugin = plugin;

        ConfigUtil.createFileIfNotExists(plugin, file);
        configuration = YamlConfiguration.loadConfiguration(file);
        loadWandIfNotExists();
    }
    public static void loadWandIfNotExists() {
        if (!configuration.isConfigurationSection("wands")) {
            configuration.createSection("wands");
        }

        for (WandType wand : WandType.values()) {
            String section = getSection(wand);
            if (!configuration.isConfigurationSection(section)) {
                set(wand, section);
            }
        }

        save();
    }

    private static void set(WandType wand, String section) {
        configuration.set(section + ".name", wand.getDefaultName());
        configuration.set(section + ".displayname", wand.getDefaultDisplayName());
        configuration.set(section + ".material", wand.getDefaultMaterial().toString());
        configuration.set(section + ".lore", wand.getDefaultLore());
    }

    public static FileConfiguration getConfiguration() {
        if (configuration == null){
            plugin.getLogger().severe("Message setup unsuccessful!");
        }
        return configuration;
    }

    public static void save(){
        try{
            configuration.save(file);
        }catch (IOException e){
            plugin.getLogger().severe(e.getMessage());
        }
    }
    public static String getName(WandType type){
        String section = getSection(type);
        return configuration.getString(section + ".name");
    }
    public static String getDisplayName(WandType type){
        String section = getSection(type);
        return configuration.getString(section + ".displayname");
    }
    public static List<String> getLore(WandType type){
        String section = getSection(type);
        return configuration.getStringList(section + ".lore");
    }


    private static String getSection(WandType type){
        return "wands." + type.getKey();
    }

    public static Material getMaterial(WandType smeltWand) {
        String section = getSection(smeltWand);
        String strMaterial = configuration.getString(section + ".material");
        Material material = null;
        try{
            material = Material.valueOf(strMaterial);
        }catch (IllegalArgumentException e){
            plugin.getLogger().severe("Invalid material provided: " + strMaterial);
        }
        Bukkit.broadcastMessage(material.toString());
        return material;
    }
}
