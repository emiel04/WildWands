package me.emiel04.wildwands.commands;

import me.emiel04.wildwands.commands.commandmanagerlib.IArgumentMatcher;
import me.emiel04.wildwands.commands.commandmanagerlib.MainCommand;
import me.emiel04.wildwands.commands.commandmanagerlib.SubCommand;

import java.util.Arrays;

public class WildWandCommand extends MainCommand {

    public WildWandCommand(String noPermissionMessage, IArgumentMatcher IArgumentMatcher) {
        super(noPermissionMessage, IArgumentMatcher);
    }

    @Override
    protected void registerSubCommands() {
        SubCommand[] commands = {
                new WildWandHelp(),
                new WildWandGive()
        };
        subCommands.addAll(Arrays.asList(commands));

    }
}
