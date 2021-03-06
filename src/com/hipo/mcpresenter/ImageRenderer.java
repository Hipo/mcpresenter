package com.hipo.mcpresenter;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

/**
 * Created by taylan on 2016-10-05.
 */
public class ImageRenderer extends MapRenderer {

    private BufferedImage image;
    private Boolean imageRendered;


    public ImageRenderer(BufferedImage image) {
        this.image = image;
        this.imageRendered = false;
    }


    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        if(imageRendered) {
            return;
        }

        canvas.drawImage(0, 0, image);
        this.imageRendered = true;
    }

}
