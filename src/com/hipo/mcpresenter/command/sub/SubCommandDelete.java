package com.hipo.mcpresenter.command.sub;

import com.hipo.mcpresenter.MCPresenterPlugin;
import com.hipo.mcpresenter.Presentation;
import com.hipo.mcpresenter.command.MCPresenterSubCommand;
import com.hipo.mcpresenter.file.PresentationFileException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by taylan on 2016-10-07.
 */
public class SubCommandDelete extends MCPresenterSubCommand {
    @Override
    public String getSub() {
        return "delete";
    }

    @Override
    public String getPermission() {
        return "mcpresenter.delete";
    }

    @Override
    public String getDescription() {
        return "Deletes an existing presentation";
    }

    @Override
    public String getSyntax() {
        return "/mcpresenter delete <presentation-id>";
    }

    /** Command "/mcpresenter delete [PRESENTATION-ID]" **/
    @Override
    public void onCommand(CommandSender sender, String[] args, String prefix) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + prefix + "This command cannot be used from the console.");
            return;
        }

        Player player = (Player)sender;

        if(args.length < 2) {
            player.sendMessage(ChatColor.RED + prefix + "Error in command syntax. Try, \"" + getSyntax() + "\"");
            return;
        }

        String presentationID = args[1];
        Presentation presentation = MCPresenterPlugin.getPresentationByID(presentationID);

        if (presentation == null) {
            player.sendMessage(ChatColor.RED + prefix + "No presentation found with name \"" + presentationID);
            return;
        }

        try {
            presentation.removeBlocks();
        }
        catch (PresentationFileException e) {
            player.sendMessage(ChatColor.RED + prefix + "Failed to remove blocks with error: \"" + e.getMessage());
        }

        presentation.delete();

        MCPresenterPlugin.getPlugin().removePresentation(presentation);

        player.sendMessage(ChatColor.GREEN + prefix + "Presentation deleted!");
    }
}
