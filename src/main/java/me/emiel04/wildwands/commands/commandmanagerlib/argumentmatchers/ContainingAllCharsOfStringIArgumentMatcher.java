package me.emiel04.wildwands.commands.commandmanagerlib.argumentmatchers;


import me.emiel04.wildwands.commands.commandmanagerlib.IArgumentMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Filters to leave all the strings that contain all the chars in argument string.
 * Example: help, toggle, reload | oe -> toggle, reload
 * Example: kill, kick, looking | li -> kill, looking
 */
public class ContainingAllCharsOfStringIArgumentMatcher implements IArgumentMatcher
{
    @Override
    public List<String> filter (List<String> tabCompletions, String argument)
    {
        List<String> result = new ArrayList<>();

        for (String tabCompletion : tabCompletions)
        {
            boolean passes = true;

            for (char c : argument.toCharArray())
            {
                passes = tabCompletion.contains(String.valueOf(c));

                if (!passes)
                    break;
            }

            if (passes)
                result.add(tabCompletion);
        }

        return result;
    }
}