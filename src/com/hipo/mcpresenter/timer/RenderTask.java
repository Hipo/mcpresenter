package com.hipo.mcpresenter.timer;

import com.hipo.mcpresenter.MCPresenterPlugin;
import com.hipo.mcpresenter.Presentation;
import com.hipo.mcpresenter.file.PresentationFileException;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by ahmetmentes on 10/6/16.
 */
public class RenderTask extends BukkitRunnable {
    @Override
    public void run() {
        Bukkit.getLogger().log(Level.WARNING, "[mcpresenter] Rendering images");
        Set<Presentation> presentations = MCPresenterPlugin.getPlugin().getPresentations();

        for(Presentation presentation : presentations) {
            try {
                presentation.renderIfImageChanged();
            } catch (PresentationFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Runtime runtime = Runtime.getRuntime();

        Bukkit.getLogger().log(Level.WARNING, "[Used / Total / Free]  " +
                (runtime.totalMemory() - runtime.freeMemory()) / 1048576L +  " MB / " +
                runtime.totalMemory() / 1048576L + " MB / " +
                runtime.freeMemory() / 1048576L + " MB");
    }
}
