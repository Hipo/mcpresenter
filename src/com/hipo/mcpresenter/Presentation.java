package com.hipo.mcpresenter;

import com.hipo.mcpresenter.file.PresentationFileException;
import com.hipo.mcpresenter.file.PresentationLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by taylan on 2016-10-04.
 */
public class Presentation {

    private BufferedImage image;
    private String presentationID;
    private UUID worldUUID;
    private int blockX;
    private int blockY;
    private int blockZ;

    private File presentationFile;


    public String getID() {
        return presentationID;
    }
    public UUID getWorldUUID() {
        return worldUUID;
    }
    public int getBlockX() {
        return blockX;
    }
    public int getBlockY() {
        return blockY;
    }
    public int getBlockZ() {
        return blockZ;
    }


    public Presentation(BufferedImage image, String presentationID, Block targetBlock) {
        this.image = image;
        this.presentationID = presentationID;

        World world = targetBlock.getWorld();

        this.worldUUID = world.getUID();
        this.blockX = targetBlock.getX();
        this.blockY = targetBlock.getY();
        this.blockZ = targetBlock.getZ();
    }

    public Presentation(File file) throws PresentationFileException {
        this.presentationFile = file;

        YamlConfiguration config = null;

        try {
            config = YamlConfiguration.loadConfiguration(presentationFile);
        }
        catch (Exception ex) {
            throw new PresentationFileException("Invalid Presentation File \"" + presentationFile.getName() + "\"");
        }

        Integer blockX = config.getInt("block_x");
        if(blockX == null) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        this.blockX = blockX;

        Integer blockY = config.getInt("block_y");
        if(blockY == null) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        this.blockY = blockY;

        Integer blockZ = config.getInt("block_z");
        if(blockZ == null) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        this.blockZ = blockZ;

        String worldUUID = config.getString("world_id");
        if(worldUUID == null) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        this.worldUUID = UUID.fromString(worldUUID);

        String presentationID = config.getString("presentation_id");
        if(presentationID == null) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        this.presentationID = presentationID;

        String b64Img = config.getString("image");
        if(b64Img == null) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        try {
            this.image = PresentationLoader.base64StringToImg(b64Img);
        }
        catch ( Exception ex ) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }
    }

    public void save() {
        if (presentationFile == null) {
            String filePath = PresentationLoader.getPresentationsDirectory() + "/" + presentationID + ".presentation";

            presentationFile = new File(filePath);

            if (!presentationFile.exists()) {
                try {
                    presentationFile.createNewFile();
                } catch (Exception ex) {
                    Bukkit.getLogger().log(Level.WARNING,
                            "[mcpresenter] Unable to create presentation file \"" + presentationID, ex);
                    return;
                }
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(presentationFile);

        config.set("presentation_id", presentationID);
        config.set("world_id", worldUUID.toString());
        config.set("block_x", blockX);
        config.set("block_y", blockY);
        config.set("block_z", blockZ);
        config.set("image", PresentationLoader.imgToBase64String(image, "png"));

        try {
            config.save(presentationFile);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING,
                    "[mcpresenter] Unable to save presentation file \"" + presentationID, e);
        }
    }

    public void delete() {
        if (presentationFile == null) {
            return;
        }

        presentationFile.delete();
    }
}
