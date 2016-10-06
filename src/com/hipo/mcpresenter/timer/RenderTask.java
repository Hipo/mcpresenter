package com.hipo.mcpresenter.timer;

import com.hipo.mcpresenter.MCPresenterPlugin;
import com.hipo.mcpresenter.Presentation;
import com.hipo.mcpresenter.file.PresentationFileException;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.logging.Level;

/**
 * Created by ahmetmentes on 10/6/16.
 */
public class RenderTask extends BukkitRunnable {
    @Override
    public void run() {
        Bukkit.getLogger().log(Level.WARNING, "[mcpresenter] Rendering images");
        Set<Presentation> presentations = MCPresenterPlugin.getPresentations();
        for(Presentation presentation : presentations) {
            try {
                presentation.renderImage();
            } catch (PresentationFileException e) {
                e.printStackTrace();
            }
        }
    }
}
