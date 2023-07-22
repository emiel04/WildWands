package me.emiel04.wildwands.config;

import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.utils.ConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LangConfig {
    private static File file;
    private static FileConfiguration configuration;
    private static WildWands plugin;
    public static void init(WildWands plugin){
        file = new File(plugin.getDataFolder(), "lang.yml");
        LangConfig.plugin = plugin;

        ConfigUtil.createFileIfNotExists(plugin, file);
        configuration = YamlConfiguration.loadConfiguration(file);
        loadMessageIfNotExist();
    }
    public static void loadMessageIfNotExist(){
        for (Lang msg :
                Lang.values()) {
            String result = configuration.getString(msg.getKey());
            if ((result == null) || (result.isEmpty())){
                set(msg);
            }
        }
        save();
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

    public static String get(Lang lang){
        return configuration.getString(lang.getKey(), lang.getDefaultMessage());
    }
    private static void set(Lang lang){
        configuration.set(lang.getKey(), lang.getDefaultMessage());
        plugin.getLogger().info("Set default value: " + lang.getKey());
    }
}

