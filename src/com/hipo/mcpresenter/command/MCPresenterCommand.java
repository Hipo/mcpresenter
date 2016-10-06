package com.hipo.mcpresenter.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by taylan on 2016-10-03.
 */
public class MCPresenterCommand implements CommandExecutor {

    public MCPresenterCommand() {
        MCPresenterSubCommand.loadCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lable,
                             String[] args) {

        String prefix = "[MCPresenterPlugin] ";

        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + prefix + "Command arguments required. Try, \"/MCPresenterPlugin help\"");
            return true;
        }

        for(MCPresenterSubCommand command : MCPresenterSubCommand.getCommands()) {
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

        sender.sendMessage(ChatColor.RED + prefix + "Unknown Command. Try, \"/MCPresenterPlugin help\"");
        return true;
    }

}
