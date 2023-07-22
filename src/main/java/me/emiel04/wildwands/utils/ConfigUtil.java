package me.emiel04.wildwands.utils;

import me.emiel04.wildwands.WildWands;

import java.io.File;
import java.io.IOException;

public class ConfigUtil {
    private ConfigUtil(){}
    public static void createFileIfNotExists(WildWands plugin, File file){
        if (!file.exists()){
            try{
                file.getParentFile().mkdirs();
                boolean success = file.createNewFile();
                if (!success){
                    plugin.getLogger().severe("Couldn't create file!");
                    plugin.getLogger().severe(file.getPath());
                }
            }catch (IOException e){
                plugin.getLogger().severe(e.getMessage());
            }
        }
    }
}
