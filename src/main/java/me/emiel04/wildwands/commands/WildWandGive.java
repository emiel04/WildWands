package me.emiel04.wildwands.commands;

import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.commands.commandmanagerlib.SubCommand;
import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import me.emiel04.wildwands.items.wands.Wand;
import me.emiel04.wildwands.utils.MessageSenderUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WildWandGive implements SubCommand {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Command used to give a wand";
    }

    @Override
    public String getSyntax() {
        return "/ww give <player> <wand> <amount> <uses>";
    }

    @Override
    public String getPermission() {
        return "wildwands.get";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        switch (index){
            case 0:{
                List<String> players = new ArrayList<>();
                for (Player p :
                        WildWands.getInstance().getServer().getOnlinePlayers()) {
                    players.add(p.getName());
                }
                return players;
            }
            case 1: {
                return WildWands.getWandManager().getAllNames();
            }
            case 2:{
                return Arrays.asList("<amount>", "1", "2", "5", "10", "16", "32", "64");
            }
            case 3: {
                return Arrays.asList("<uses>", "-1", "1", "2", "3", "4", "5");
            }
        }
        return WildWands.getWandManager().getAllNames();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 4){
            new WildWandHelp().perform(sender, args);
            return;
        }
        String playerName = args[0], wandArg = args[1], amountArg = args[2], usesArg = args[3];
        if (!MessageSenderUtil.isInteger(amountArg)){
            MessageSenderUtil.sendErrorWithPrefix(sender, "Amount must be an integer!");
            return;
        }
        if (!MessageSenderUtil.isInteger(usesArg)){
            MessageSenderUtil.sendErrorWithPrefix(sender, "Uses must be an integer!");
            return;
        }
        if (!WildWands.getWandManager().getAllNames().contains(wandArg)){
            MessageSenderUtil.sendErrorWithPrefix(sender, "That wand was not found!");
            return;
        }
        Player target = Bukkit.getPlayer(playerName);
        if (target == null || !target.isOnline()){
            MessageSenderUtil.sendErrorWithPrefix(sender, "That player is not online!");
            return;
        }
        int amount = Integer.parseInt(amountArg), uses = Integer.parseInt(usesArg);
        Wand wand = WildWands.getWandManager().getWandByName(wandArg);
        for (int i = 0; i < amount; i++) {
            target.getInventory().addItem(wand.getItem(uses));
        }
        MessageSenderUtil.sendMessageWithPrefix(sender,
                LangConfig.get(Lang.GIVEN_WAND)
                        .replace("%wand%", wand.getName())
                        .replace("%uses%", ""+uses)
                        .replace("%target%", target.getDisplayName()));
        MessageSenderUtil.sendMessageWithPrefix(target,
                LangConfig.get(Lang.RECEIVED_WAND)
                        .replace("%wand%", wand.getName())
                        .replace("%uses%", ""+uses));

    }
}
