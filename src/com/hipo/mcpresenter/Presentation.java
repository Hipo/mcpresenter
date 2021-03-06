package com.hipo.mcpresenter;

import com.hipo.mcpresenter.file.PresentationFileException;
import com.hipo.mcpresenter.file.PresentationLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by taylan on 2016-10-04.
 */
public class Presentation {

    private URL url;
    private String presentationID;
    private UUID worldUUID;
    private int blockX;
    private int blockY;
    private int blockZ;
    private String blockDirection;
    private String eTag;

    private File presentationFile;


    public String getID() {
        return presentationID;
    }


    public Presentation(URL url, String presentationID, Block targetBlock, String direction) {
        this.url = url;
        this.presentationID = presentationID;

        World world = targetBlock.getWorld();

        this.worldUUID = world.getUID();
        this.blockX = targetBlock.getX();
        this.blockY = targetBlock.getY();
        this.blockZ = targetBlock.getZ();
        this.blockDirection = direction;
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

        String direction = config.getString("direction");
        if(direction == null) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        this.blockDirection = direction;

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

        String url = config.getString("url");
        if(url == null) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        try {
            this.url = new URL(url);
        }
        catch (MalformedURLException ex) {
            throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
        }

        this.eTag = config.getString("e_tag");
        if(eTag == null) {
            eTag = getCurrentETag();
            try {
                updateConfig();
            } catch (IOException e) {
                throw new PresentationFileException("Unable to load presentation file \"" + presentationFile.getName());
            }
        }
    }

    public void generateBlocks() throws PresentationFileException {
        World world = Bukkit.getWorld(worldUUID);
        BlockFace blockFace = getBlockFace(blockDirection);
        Set<ItemFrame> iFrames = new HashSet<ItemFrame>();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 8; y++) {
                Location loc = null;
                Location backLoc = null;

                if(blockDirection.equalsIgnoreCase("north")) {
                    loc = new Location(world, blockX + x, blockY - y , blockZ + 1);
                    backLoc = new Location(world, blockX + x, blockY - y , blockZ);
                }else if(blockDirection.equalsIgnoreCase("south")) {
                    loc = new Location(world, blockX - x, blockY - y , blockZ - 1);
                    backLoc = new Location(world, blockX - x, blockY - y , blockZ);
                }else if(blockDirection.equalsIgnoreCase("east")) {
                    loc = new Location(world, blockX - 1 , blockY - y , blockZ + x);
                    backLoc = new Location(world, blockX , blockY - y , blockZ + x);
                } else if(blockDirection.equalsIgnoreCase("west")) {
                    loc = new Location(world, blockX + 1, blockY - y , blockZ - x);
                    backLoc = new Location(world, blockX, blockY - y , blockZ - x);
                }

                if(loc == null) {
                    throw new PresentationFileException("Unable to generate location for \"" + presentationID);
                }

                if(loc.getBlock().getType() != Material.AIR || backLoc.getBlock().getType() == Material.AIR) {
                    for(ItemFrame iFrame : iFrames) {
                        iFrame.remove();
                    }

                    throw new PresentationFileException("Not enough blocks for \"" + presentationID);
                }

                try {
                    ItemFrame iFrame = (ItemFrame)world.spawnEntity(loc, EntityType.ITEM_FRAME);

                    iFrame.setFacingDirection(blockFace);

                    iFrames.add(iFrame);
                }
                catch(Exception ex) {
                    for(ItemFrame iFrame : iFrames) {
                        iFrame.remove();
                    }

                    throw new PresentationFileException("Cannot generate blocks for \"" + presentationID);
                }
            }
        }
    }

    private BlockFace getBlockFace(String direction) {
        switch(direction) {
            case "north":
                return BlockFace.SOUTH;
            case "south":
                return BlockFace.NORTH;
            case "east":
                return BlockFace.WEST;
            case "west":
                return BlockFace.EAST;
        }

        return BlockFace.NORTH;
    }

    public void renderImage() throws PresentationFileException {
        BufferedImage image;

        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new PresentationFileException("Unable to load image from " + url);
        }

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int targetWidth = 1280;
        int targetHeight = 1024;

        // Image needs to be resized to 1280x1024 here, with aspect ratio being retained
        // If empty spaces are created around the image, these need to be filled with black color
        if (imageWidth != targetWidth || imageHeight != targetHeight) {
            if (imageWidth > imageHeight) {
                targetHeight = imageHeight * targetWidth / imageWidth;
            } else {
                targetWidth = imageWidth * targetHeight / imageHeight;
            }

            Image scaledImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

            image = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = image.createGraphics();

            g2d.setBackground(Color.black);
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, 1280, 1024);
            g2d.drawImage(scaledImage, (1280 - targetWidth) / 2, (1024 - targetHeight) / 2, null);
            g2d.dispose();
        }

        World world = Bukkit.getWorld(worldUUID);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 8; y++) {
                // We slice the resized image into 128x128 chunks
                BufferedImage subImage;

                try {
                    subImage = image.getSubimage(x * 128, y * 128, 128, 128);
                }
                catch (RasterFormatException e) {
                    throw new PresentationFileException("Unable to extract sub-image at coordinates: " + x + "," + y);
                }

                MapView mapView = Bukkit.createMap(world);

                mapView.getRenderers().clear();
                mapView.addRenderer(new ImageRenderer(subImage));

                Location loc = null;

                if(blockDirection.equalsIgnoreCase("north")) {
                    loc = new Location(world, blockX + x, blockY - y , blockZ + 1);
                }else if(blockDirection.equalsIgnoreCase("south")) {
                    loc = new Location(world, blockX - x, blockY - y , blockZ - 1);
                }else if(blockDirection.equalsIgnoreCase("east")) {
                    loc = new Location(world, blockX - 1 , blockY - y , blockZ + x);
                } else if(blockDirection.equalsIgnoreCase("west")) {
                    loc = new Location(world, blockX + 1, blockY - y , blockZ - x);
                }

                if (loc == null) {
                    throw new PresentationFileException("Unable to find location");
                }

                Collection<Entity> entities = world.getNearbyEntities(loc, 1, 1, 1);

                if (entities.isEmpty()) {
                    throw new PresentationFileException("Unable to find entities at location");
                }

                ItemFrame iFrame = null;

                if (entities.size() == 1) {
                    iFrame = (ItemFrame) entities.toArray()[0];
                } else {
                    for (Entity entity : entities) {
                        if (entity.getType() != EntityType.ITEM_FRAME) {
                            continue;
                        }

                        Location entityLoc = entity.getLocation();

                        if (!entityLoc.getBlock().getLocation().equals(loc)) {
                            continue;
                        }

                        iFrame = (ItemFrame) entity;
                    }
                }

                if (iFrame == null) {
                    throw new PresentationFileException("Unable to find entities at location");
                }

                ItemStack iStack = new ItemStack(Material.MAP, 1);

                iStack.setDurability(mapView.getId());
                iFrame.setItem(iStack);
            }
        }
    }

    public String getCurrentETag() throws PresentationFileException {
        try {
            HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
            conn.setRequestMethod("HEAD");
            return conn.getHeaderField("Etag");
        } catch (IOException e) {
            throw new PresentationFileException("Couldn't get ETag");
        }
    }

    public void renderIfImageChanged() throws PresentationFileException, IOException {
        String currentEtag = getCurrentETag();
        if(!eTag.equalsIgnoreCase(currentEtag)) {
            renderImage();
            this.eTag = currentEtag;
            updateConfig();
        }
    }

    public void updateConfig() throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(presentationFile);

        config.set("presentation_id", presentationID);
        config.set("world_id", worldUUID.toString());
        config.set("block_x", blockX);
        config.set("block_y", blockY);
        config.set("block_z", blockZ);
        config.set("direction", blockDirection);
        config.set("url", url.toString());
        config.set("e_tag", eTag);

        config.save(presentationFile);
    }

    public void save() throws IOException {
        if (presentationFile == null) {
            String filePath = PresentationLoader.getPresentationsDirectory() + "/" + presentationID + ".presentation";

            presentationFile = new File(filePath);

            if (!presentationFile.exists()) {
                presentationFile.createNewFile();
            }
        }
        updateConfig();

    }

    public void removeBlocks() throws PresentationFileException {
        World world = Bukkit.getWorld(worldUUID);
        BlockFace blockFace = getBlockFace(blockDirection);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 8; y++) {
                Location loc = null;

                if(blockDirection.equalsIgnoreCase("north")) {
                    loc = new Location(world, blockX + x, blockY - y , blockZ + 1);
                }else if(blockDirection.equalsIgnoreCase("south")) {
                    loc = new Location(world, blockX - x, blockY - y , blockZ - 1);
                }else if(blockDirection.equalsIgnoreCase("east")) {
                    loc = new Location(world, blockX - 1 , blockY - y , blockZ + x);
                } else if(blockDirection.equalsIgnoreCase("west")) {
                    loc = new Location(world, blockX + 1, blockY - y , blockZ - x);
                }

                if (loc == null) {
                    throw new PresentationFileException("Unable to find location");
                }

                Collection<Entity> entities = world.getNearbyEntities(loc, 1, 1, 1);

                if (entities.isEmpty()) {
                    throw new PresentationFileException("Unable to find entities at location");
                }

                ItemFrame iFrame = null;

                if (entities.size() == 1) {
                    iFrame = (ItemFrame) entities.toArray()[0];
                } else {
                    for (Entity entity : entities) {
                        if (entity.getType() != EntityType.ITEM_FRAME) {
                            continue;
                        }

                        Location entityLoc = entity.getLocation();

                        if (!entityLoc.getBlock().getLocation().equals(loc)) {
                            continue;
                        }

                        iFrame = (ItemFrame) entity;
                    }
                }

                if (iFrame == null) {
                    throw new PresentationFileException("Unable to find entities at location");
                }

                iFrame.remove();
            }
        }
    }

    public void delete() {
        if (presentationFile == null) {
            return;
        }

        presentationFile.delete();
    }
}
