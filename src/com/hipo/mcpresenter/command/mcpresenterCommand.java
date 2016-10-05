package com.hipo.mcpresenter.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by taylan on 2016-10-03.
 */
public class mcpresenterCommand implements CommandExecutor {

    public mcpresenterCommand() {
        mcpresenterSubCommand.loadCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lable,
                             String[] args) {

        String prefix = "[mcpresenterPlugin] ";

        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + prefix + "Command arguments required. Try, \"/mcpresenterPlugin help\"");
            return true;
        }

        for(mcpresenterSubCommand command : mcpresenterSubCommand.getCommands()) {
            if(!command.getSub().equalsIgnoreCase(args[0])) {
                continue;
            }

//            if(command.getPermission() != null) {
//                if(sender instanceof Player && !SketchMapUtils
//                        .hasPermission((Player) sender, command.getPermission())) {
//                    sender.sendMessage(ChatColor.RED + prefix + "You do not have permission to use this command.");
//                    return true;
//                }
//            }

            command.onCommand(sender, args, prefix);
            return true;
        }

        sender.sendMessage(ChatColor.RED + prefix + "Unknown Command. Try, \"/mcpresenterPlugin help\"");
        return true;
    }

}
