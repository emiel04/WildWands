package me.emiel04.wildwands.commands;

import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.config.WandConfig;
import me.emiel04.wildwands.items.wands.Wand;
import me.emiel04.wildwands.items.wands.WandType;
import me.emiel04.wildwands.utils.MessageSenderUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Tescommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        MessageSenderUtil.sendErrorWithPrefix(commandSender, "This didn't work!");
        MessageSenderUtil.sendMessage(commandSender,
                LangConfig.get(Lang.GIVEN_MESSAGE)
                        .replace("%wand%", WandConfig.getName(WandType.SMELT_WAND)));
        Wand sw = WildWands.getWandManager().get(WandType.SMELT_WAND);
        ItemStack wand = sw.getItem(10);
        if (!(commandSender instanceof Player)){
            return true;
        }
        Player p = (Player) commandSender;
        p.getInventory().addItem(wand);
        return true;
    }
}
