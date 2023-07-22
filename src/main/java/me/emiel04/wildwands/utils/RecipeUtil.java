package me.emiel04.wildwands.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;

public class RecipeUtil {

    private static ItemStack getSmeltedVariant(ItemStack input) {
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            Recipe recipe = recipes.next();
            if (recipe instanceof FurnaceRecipe) {
                FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
                RecipeChoice choice = furnaceRecipe.getInputChoice();
                if (choice.test(input)) {
                    ItemStack predone = furnaceRecipe.getResult();
                    return predone;
                }
            }
        }
        return new ItemStack(Material.AIR);
    }

    private static final HashMap<Material, ItemStack> smeltCache = new HashMap<>();

    public static ItemStack getSmeltedVariant(ItemStack input, boolean updateCache) {
        ItemStack out;
        if (updateCache) {
            out = getSmeltedVariant(input);
            smeltCache.put(input.getType(), out.clone());
        } else {
            out = smeltCache.getOrDefault(input.getType(), new ItemStack(Material.AIR)).clone();
        }
        out.setAmount(out.getAmount()*input.getAmount());
        return out;
    }

    private static final HashMap<Material, Long> cacheDuration = new HashMap<>();
    public static final int REFRESH_EVERY = 60000; // Refresh every minute

    public static ItemStack getSmeltedVariantCached(ItemStack input) {
        if (System.currentTimeMillis() - cacheDuration.getOrDefault(input.getType(), Instant.EPOCH.toEpochMilli()) > REFRESH_EVERY) {
            cacheDuration.put(input.getType(), System.currentTimeMillis());
            return getSmeltedVariant(input, true);
        } else {
            return getSmeltedVariant(input, false);
        }
    }
}