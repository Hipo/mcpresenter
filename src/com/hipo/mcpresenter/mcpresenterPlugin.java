package com.hipo.mcpresenter;

import com.hipo.mcpresenter.command.mcpresenterCommand;

import com.hipo.mcpresenter.file.PresentationFileException;
import com.hipo.mcpresenter.file.PresentationLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by taylan on 2016-10-03.
 */
public class mcpresenterPlugin extends JavaPlugin {

    private static mcpresenterPlugin plugin;
    private static Set<Presentation> presentations;

    @Override
    public void onEnable() {
        plugin = this;
        presentations = new HashSet<Presentation>();

        loadPresentations();

        this.getCommand("mcpresenter").setExecutor(new mcpresenterCommand());
    }

    @Override
    public void onDisable() {

    }

    public static void loadPresentations() {
        for(File file : PresentationLoader.getPresentationsDirectory().listFiles()) {
            if(!file.getName().endsWith(".presentation")) {
                continue;
            }

            try {
                presentations.add(new Presentation(file));
            }
            catch (PresentationFileException ex) {
                Bukkit.getLogger().log(Level.WARNING, ex.getMessage(), ex);
            }

        }
    }

    public static mcpresenterPlugin getPlugin() {
        return plugin;
    }

    public static Presentation getPresentationByID(String id) {
        for(Presentation presentation : presentations) {
            if(presentation.getID().equalsIgnoreCase(id)) {
                return presentation;
            }
        }

        return null;
    }

}
