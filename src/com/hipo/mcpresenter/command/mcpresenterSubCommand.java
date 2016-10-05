package com.hipo.mcpresenter.command;

import com.hipo.mcpresenter.command.sub.SubCommandCreate;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taylan on 2016-10-04.
 */
public abstract class mcpresenterSubCommand {

    private static List<mcpresenterSubCommand> commands;

    public abstract String getSub();
    public abstract String getPermission();
    public abstract String getDescription();
    public abstract String getSyntax();

    public abstract void onCommand(CommandSender sender,
                                   String[] args, String prefix );

    public static void loadCommands() {
        commands = new ArrayList<mcpresenterSubCommand>();

        loadCommand(new SubCommandCreate());

    }

    private static void loadCommand(mcpresenterSubCommand sub) {
        commands.add(sub);
    }

    public static List<mcpresenterSubCommand> getCommands() {
        return commands;
    }
}
