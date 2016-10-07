package com.hipo.mcpresenter.command.sub;

import com.hipo.mcpresenter.MCPresenterPlugin;
import com.hipo.mcpresenter.Presentation;
import com.hipo.mcpresenter.command.MCPresenterSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by taylan on 2016-10-07.
 */
public class SubCommandList extends MCPresenterSubCommand {

    @Override
    public String getSub() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "mcpresenter.list";
    }

    @Override
    public String getDescription() {
        return "Lists existing presentations";
    }

    @Override
    public String getSyntax() {
        return "/mcpresenter list";
    }

    /** Command "/mcpresenter list" **/
    @Override
    public void onCommand(CommandSender sender, String[] args, String prefix) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + prefix + "This command cannot be used from the console.");
            return;
        }

        Player player = (Player)sender;

        for (Presentation presentation : MCPresenterPlugin.getPlugin().getPresentations()) {
            player.sendMessage(ChatColor.GREEN + prefix + "- " + presentation.getID());
        }
    }

}
