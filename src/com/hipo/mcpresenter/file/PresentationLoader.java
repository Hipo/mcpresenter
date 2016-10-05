package com.hipo.mcpresenter.file;

import com.hipo.mcpresenter.mcpresenterPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by taylan on 2016-10-04.
 */
public class PresentationLoader {
    private static File dataFolder;
    private static File presentationsDirectory;

    public static File getDataFolder() {
        if(dataFolder != null) {
            return dataFolder;
        }

        dataFolder = mcpresenterPlugin.getPlugin().getDataFolder();

        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        return dataFolder;
    }

    public static File getPresentationsDirectory() {
        if(presentationsDirectory != null) {
            return presentationsDirectory;
        }

        presentationsDirectory = new File(getDataFolder().toString() + "/" + "presentations/");

        if(!presentationsDirectory.exists()) {
            presentationsDirectory.mkdirs();
        }

        return presentationsDirectory;
    }

    public static String imgToBase64String(final RenderedImage img, final String formatName) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, formatName, Base64.getEncoder().wrap(os));
            return os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static BufferedImage base64StringToImg(final String base64String) {
        try {
            return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64String)));
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
}
