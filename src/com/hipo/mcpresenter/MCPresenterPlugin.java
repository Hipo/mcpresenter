package com.hipo.mcpresenter;

import com.hipo.mcpresenter.command.MCPresenterCommand;

import com.hipo.mcpresenter.file.PresentationFileException;
import com.hipo.mcpresenter.file.PresentationLoader;
import com.hipo.mcpresenter.timer.RenderTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by taylan on 2016-10-03.
 */
public class MCPresenterPlugin extends JavaPlugin {

    private static MCPresenterPlugin plugin;
    private static Set<Presentation> presentations;

    private static final long INTERVAL = 20 * 60;

    @Override
    public void onEnable() {
        plugin = this;
        presentations = new HashSet<Presentation>();

        Bukkit.getLogger().log(Level.WARNING, "[mcpresenter] Plugin online, loading presentations...");

        loadPresentations();

        new RenderTask().runTaskTimerAsynchronously(plugin, 100, INTERVAL);

        this.getCommand("mcpresenter").setExecutor(new MCPresenterCommand());
    }

    @Override
    public void onDisable() {

    }

    public static void loadPresentations() {
        for(File file : PresentationLoader.getPresentationsDirectory().listFiles()) {
            if(!file.getName().endsWith(".presentation")) {
                continue;
            }

            Bukkit.getLogger().log(Level.WARNING, "[mcpresenter] Loading " + file.getName());

            try {
                presentations.add(new Presentation(file));
            }
            catch (PresentationFileException ex) {
                Bukkit.getLogger().log(Level.WARNING, ex.getMessage(), ex);
            }

        }
    }

    public static void addPresentation(Presentation presentation) {
        presentations.add(presentation);
    }

    public static MCPresenterPlugin getPlugin() {
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

    public static Set<Presentation> getPresentations() {
        return presentations;
    }

}
