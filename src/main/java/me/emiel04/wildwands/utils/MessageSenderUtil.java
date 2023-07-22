package me.emiel04.wildwands.utils;

import me.emiel04.wildwands.WildWands;
import me.emiel04.wildwands.config.Lang;
import me.emiel04.wildwands.config.LangConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageSenderUtil {
    private static final FileConfiguration config = WildWands.getInstance().getConfig();
    private static final String mainColor = config.getString("main_color");
    private static final String prefix = mainColor + LangConfig.get(Lang.PREFIX);
    private static final String between = config.getString("between");
    private static final String errorColor = config.getString("error_color");

    private static void send(CommandSender target, String raw) {
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', raw));
    }

    public static String getMainColor() {
        return mainColor;
    }
    public static String getPrefix(){
        return prefix;
    }
    public static void sendMessage(CommandSender sender, String message) {
        send(sender, message);
    }

    public static void sendMessageWithPrefix(CommandSender sender, String message) {
        send(sender, prefix + between + message);
    }

    public static void sendErrorWithPrefix(CommandSender sender, String message) {
        send(sender, prefix + between + errorColor + message);
    }

    public static void sendWrongCommandUsage(CommandSender sender) {
        sendErrorWithPrefix(sender, "Please use the command like this: ");
    }

    public static void broadcastMessageWithPrefix(String message) {
        Bukkit.broadcastMessage(prefix + between + message);
    }

    public static void sendLine(CommandSender sender) {
        send(sender, mainColor + "&m&l                                  ");
    }
    public static void sendEmpty(CommandSender sender) {
        send(sender, "  ");
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
