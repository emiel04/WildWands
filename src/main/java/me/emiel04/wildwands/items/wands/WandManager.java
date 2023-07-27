package me.emiel04.wildwands.items.wands;

import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.items.PixieDust;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WandManager {
    private final WildWands plugin;
    private final List<Wand> wands;
    private final PixieDust pixieDust;

    public WandManager(WildWands plugin, List<Wand> wands, PixieDust dust) {
        this.plugin = plugin;
        this.wands = wands;
        this.pixieDust = dust;
        addListeners();
    }

    private void addListeners() {
        for (Wand w :
                wands) {
            plugin.getServer().getPluginManager().registerEvents(w, plugin);
        }
        plugin.getServer().getPluginManager().registerEvents(pixieDust, plugin);
    }

    public Wand get(WandType type) {
        for (Wand w :
                wands) {
            if (w.getType().equals(type)) {
                return w;
            }
        }
        return null;
    }

    public List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        for (Wand w :
                wands) {
            names.add(w.getName());
        }
        names.add(pixieDust.getName());
        return names;
    }
    public List<String> getAllWandNames() {
        List<String> names = new ArrayList<>();
        for (Wand w :
                wands) {
            names.add(w.getName());
        }
        return names;
    }
    public WandType getWandTypeByName(String name){
        for (Wand w :
                wands) {
            if (w.getName().equalsIgnoreCase(name)){
                return w.getType();
            }
        }
        return null;
    }
    public Wand getWandByName(String name){
        for (Wand w :
                wands) {
            if (w.getName().equalsIgnoreCase(name)){
                return w;
            }
        }
        return null;
    }
    public List<Wand> getWands() {
        return wands;
    }
    public PixieDust getPixieDust() {
        return pixieDust;
    }

}
