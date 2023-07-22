package me.emiel04.wildwands.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Worth;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;

public class EssUtil {
    private EssUtil(){
        // Static class
    }
    private static Worth worth;
    private static Essentials essentials;

    public static void setup(){
        essentials = (Essentials) JavaPlugin.getPlugin(Essentials.class);
        worth = essentials.getWorth();
    }

    public static BigDecimal getPrice(ItemStack itemStack){
        return worth.getPrice(essentials, itemStack);
    }
    public static Worth getWorth() {
        return worth;
    }

    public static Essentials getEssentials() {
        return essentials;
    }
}
