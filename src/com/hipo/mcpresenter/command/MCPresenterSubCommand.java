package com.hipo.mcpresenter.command;

import com.hipo.mcpresenter.command.sub.SubCommandCreate;

import com.hipo.mcpresenter.command.sub.SubCommandDelete;
import com.hipo.mcpresenter.command.sub.SubCommandRender;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taylan on 2016-10-04.
 */
public abstract class MCPresenterSubCommand {

    private static List<MCPresenterSubCommand> commands;

    public abstract String getSub();
    public abstract String getPermission();
    public abstract String getDescription();
    public abstract String getSyntax();

    public abstract void onCommand(CommandSender sender,
                                   String[] args, String prefix );

    public static void loadCommands() {
        commands = new ArrayList<MCPresenterSubCommand>();

        commands.add(new SubCommandCreate());
        commands.add(new SubCommandRender());
        commands.add(new SubCommandDelete());

    }

    public static List<MCPresenterSubCommand> getCommands() {
        return commands;
    }
}
