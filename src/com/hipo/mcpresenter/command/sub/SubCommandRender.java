package com.hipo.mcpresenter.command.sub;

import com.hipo.mcpresenter.MCPresenterPlugin;
import com.hipo.mcpresenter.Presentation;
import com.hipo.mcpresenter.command.MCPresenterSubCommand;
import com.hipo.mcpresenter.file.PresentationFileException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by taylan on 2016-10-05.
 */
public class SubCommandRender extends MCPresenterSubCommand {
    @Override
    public String getSub() {
        return "render";
    }

    @Override
    public String getPermission() {
        return "mcpresenter.render";
    }

    @Override
    public String getDescription() {
        return "Renders an existing presentation on its designated spot";
    }

    @Override
    public String getSyntax() {
        return "/mcpresenter render <presentation-id>";
    }

    /** Command "/mcpresenter render [PRESENTATION-ID]" **/
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
            presentation.renderImage();
        }
        catch (PresentationFileException e) {
            player.sendMessage(ChatColor.RED + prefix + "Failed to render with error: \"" + e.getMessage());
            return;
        }

        player.sendMessage(ChatColor.GREEN + prefix + "Render completed!");
    }
}
