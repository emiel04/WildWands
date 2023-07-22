package me.emiel04.wildwands.commands;

import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.commands.commandmanagerlib.SubCommand;
import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.items.wands.Wand;
import me.emiel04.wildwands.utils.MessageSenderUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WildWandHelp implements SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Help command";
    }

    @Override
    public String getSyntax() {
        return "/ww help";
    }

    @Override
    public String getPermission() {
        return "wildwands.help";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
       return new ArrayList<>();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        SubCommand[] commands = {
                new WildWandGive()
        };


        MessageSenderUtil.sendEmpty(sender);
        MessageSenderUtil.sendMessage(sender, MessageSenderUtil.getMainColor() + MessageSenderUtil.getPrefix() + ChatColor.WHITE + " Commands");
        for (SubCommand command :
                commands) {
            MessageSenderUtil.sendMessage(sender, "• " + command.getSyntax() + " | " + command.getDescription());
        }
        MessageSenderUtil.sendEmpty(sender);
    }
}
