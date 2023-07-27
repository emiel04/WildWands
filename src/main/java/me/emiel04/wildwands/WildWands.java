package me.emiel04.wildwands;

import me.emiel04.wildwands.commands.WildWandCommand;
import me.emiel04.wildwands.commands.commandmanagerlib.argumentmatchers.ContainingStringIArgumentMatcher;
import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.config.NormalConfig;
import me.emiel04.wildwands.config.WandConfig;
import me.emiel04.wildwands.items.PixieDust;
import me.emiel04.wildwands.items.wands.*;
import me.emiel04.wildwands.utils.EssUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class WildWands extends JavaPlugin {

    private static WildWands instance;
    private static WandManager wandManager;
    private static Economy econ = null;
    public static WildWands getInstance() {
        return instance;
    }

    public static WandManager getWandManager() {
        return wandManager;
    }
    public static Economy getEconomy() {
        return econ;
    }
    public static boolean essentialsAndVaultLoaded = false;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        LangConfig.init(instance);
        WandConfig.init(instance);
        saveDefaultConfig();

        if (
                getServer().getPluginManager().getPlugin("Essentials") != null &&
                getServer().getPluginManager().getPlugin("Vault") != null
        ) {
            essentialsAndVaultLoaded = true;
            getLogger().info("Vault and EssentialsX found, sell wand will work.");
        }else{
            essentialsAndVaultLoaded = false;
            getLogger().warning("Vault and/or essentialsX not found, sell wand won't work.");
        }

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        EssUtil.setup();

        initializeWands();
        addCommandsAndListeners();

    }

    private void initializeWands() {
        List<Wand> wands = new ArrayList<>(Arrays.asList(
                Wand.createWand(WandType.SMELT_WAND),
                Wand.createWand(WandType.CONDENSE_WAND)
        ));

        if (essentialsAndVaultLoaded){
            Wand wand = Wand.createWand(WandType.SELL_WAND);
            wands.add(wand);
        }
        PixieDust dust = new PixieDust(NormalConfig.getName(), NormalConfig.getDisplayName(), NormalConfig.getLore(), NormalConfig.getMaterial(),NormalConfig.getGlow());
        wandManager = new WandManager(this, wands, dust);
    }

    private void addCommandsAndListeners() {
        this.getCommand("wildwands").setExecutor(new WildWandCommand(LangConfig.get(Lang.NO_PERM), new ContainingStringIArgumentMatcher()));

    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
