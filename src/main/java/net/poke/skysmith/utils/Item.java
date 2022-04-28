package net.poke.skysmith.utils;

import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.poke.skysmith.Main;
import org.bson.Document;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Item {
    public static HashMap<String, ArrayList<Item>> memberItems = new HashMap<>();
    public static HashMap<String, Item> currentlyEditing = new HashMap<>();
    public static HashMap<String, String> KEYS = new HashMap<>();
    public String name;
    public String id;
    public List<String> description;
    public String rarity;
    public List<String> lore;
    public HashMap<Stat, Integer> stats;
    public Boolean pureLore = false;
    public boolean numberedLore = false;
    private int format = 0;

    public Item(String id, String name, List<String> description, String rarity) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.description = description;
        lore = new ArrayList<>();
        stats = new HashMap<>();
    }

    public Item addLine(String line) {
        description.add(line);
        return this;
    }

    public Item removeLine(int index) {
        description.remove(index);
        return this;
    }

    public Item setLine(int index, String str) {
        Utils.setLoreLine(index, this, str);
        return this;
    }

    public static Item getItem(String memberID, String itemID) {
        for (Item item : memberItems.get(memberID)) {
            if (item.id.equals(itemID)) {
                return item;
            }
        }
        return null;
    }

    private boolean hasMagic() {
        for (String str : lore) {
            if (str.contains("&k")) {
                return true;
            }
        }
        return false;
    }

    private int width() {
        List<Integer> sizes = new ArrayList<>();
        BufferedImage bufferedImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setFont(Main.regularFont);
        boolean checking = false;
        Color lastColor = Color.WHITE;
        for (String l : lore) {
            int size = 0;
            for (char c : l.toCharArray()) {
                String line = String.valueOf(c);
                if (line.equals("&")) {
                    checking = true;
                    continue;
                }
                if (checking) {
                    if (ColorCodes.ALL_CODES.contains(line)) {
                        lastColor = check2(line, lastColor, graphics);
                        checking = false;
                        continue;
                    }
                    checking = false;
                }
                size += graphics.getFontMetrics().stringWidth(String.valueOf(c));
            }
            sizes.add(size);
        }
        //return the largest size in the array
        return sizes.stream().max(Integer::compareTo).get();
    }

    private int height(Font font) {
        BufferedImage bufferedImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setFont(font);
        return graphics.getFontMetrics().getHeight();
    }

    public String build(String guild, String member) {
        long ms = System.currentTimeMillis();
        lore = new ArrayList<>();
        lore.add(name);
        if (!stats.isEmpty() && !pureLore) {
            for (Stat stat : stats.keySet()) {
                lore.add("&7" + stat.displayName + ": " + stat.color + "+" + stats.get(stat) + stat.suffix);
            }
            lore.add("");
        }
        if (numberedLore) {
            for (int i = 0; i < lore.size(); i++) {
                lore.add("&c" + (i + 1) + ". &r" + lore.get(i));
            }
        }
        lore.addAll(description);
        lore.add(rarity);
        int width = 14 + width();
        int height = 10 + lore.size() * height(Main.regularFont);
        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setFont(Main.regularFont);
        float fontHeight = 0;
        format = 0;
        fontHeight = graphics.getFontMetrics().getHeight();

        graphics.setColor(new Color(44, 8, 99, 255));
        graphics.drawRect(2, 2, width - 5, height - 5);

        graphics.setColor(new Color(85, 85, 85));

        for (int i = 0; i < lore.size(); i++) {
            char[] charArray = lore.get(i).toCharArray();
            int h = 0;
            Color lastColor = Color.WHITE;
            Color lastColor2 = Color.decode("#3f3f3f");
            boolean checking = false;
            for (char c : charArray) {
                int xoffset = 0;
                int yoffset = 0;
                String line = String.valueOf(c);
                if (!graphics.getFont().canDisplay(c)) {
                    graphics.setFont(Main.unicodeFont);
                } else {
                    if (format == 1) {
                        graphics.setFont(Main.boldFont);
                        fontHeight = graphics.getFontMetrics().getHeight();
                    } else {
                        graphics.setFont(Main.regularFont);
                        fontHeight = graphics.getFontMetrics().getHeight();
                    }
                }
                if (line.equals("&")) {
                    checking = true;
                    continue;
                }
                if (checking) {
                    if (ColorCodes.ALL_CODES.contains(line)) {
                        lastColor = check(line, lastColor, graphics);
                        lastColor2 = check2(line, lastColor2, graphics);
                        checking = false;
                        continue;
                    }
                    checking = false;
                    graphics.setColor(lastColor2);
                    graphics.drawString("&", 10 + h + xoffset, (fontHeight + (i) * fontHeight) + yoffset);
                    graphics.setColor(lastColor);
                    graphics.drawString("&", 8 + h + xoffset, (fontHeight - 2 + (i) * fontHeight) + yoffset);
                    h += graphics.getFontMetrics().stringWidth("&");
                }
                graphics.setColor(lastColor2);
                graphics.drawString(line, 10 + h + xoffset, (fontHeight + (i) * fontHeight) + yoffset);
                graphics.setColor(lastColor);
                graphics.drawString(line, 8 + h + xoffset, ((fontHeight - 2) + ((i) * fontHeight)) + yoffset);
                h += graphics.getFontMetrics().stringWidth(line);
            }
            format = 0;
        }
        if (!hasMagic()) {
            System.out.println("Created image in " + (System.currentTimeMillis() - ms) + "ms");
            String url = null;
            try {
                new File(guild).mkdir();
                File file = new File(guild + "/" + "Skysmith-" + member + ".png");
                file.createNewFile();
                ImageIO.write(bufferedImage, "png", file);
                try {
                    Uploader uploader = new FileUploader(Main.client, file);
                    com.uploadcare.api.File file2 = uploader.upload().save();
                    url = file2.getOriginalFileUrl().toString();
                } catch (UploadFailureException e) {
                    System.out.println("Upload failed :(");
                }
            } catch (IOException e) {
                System.out.println("Upload failed :(");
            }
            System.out.println("Got url of image in " + (System.currentTimeMillis() - ms) + "ms");
            return url;
        } else {
            return buildMagic(guild, member, ms);
        }
    }

    String magic = "!#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»";

    private String randomMagic() {
        return String.valueOf(magic.charAt(new Random().nextInt(magic.length())));
    }

    private String buildMagic(String guild, String member, Long ms) {
        ArrayList<BufferedImage> images = new ArrayList<>();
        ArrayList<String> newLore = new ArrayList<>();
        for (int e = 0; e < 25; e++) {
            int width = 14 + width();
            int height = 10 + lore.size() * height(Main.regularFont);
            BufferedImage firstImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = firstImage.createGraphics();
            g.setFont(Main.regularFont);
            float fontHeight1 = 0;
            format = 0;
            fontHeight1 = g.getFontMetrics().getHeight();

            g.setColor(new Color(44, 8, 99, 255));
            g.drawRect(2, 2, width - 5, height - 5);

            g.setColor(new Color(85, 85, 85));

            for (int i = 0; i < lore.size(); i++) {
                char[] charArray = lore.get(i).toCharArray();
                int h = 0;
                Color lastColor = Color.WHITE;
                Color lastColor2 = Color.decode("#3f3f3f");
                boolean checking = false;
                for (char c : charArray) {
                    String magicChar = randomMagic();
                    int xoffset = 0;
                    int yoffset = 0;
                    String line = String.valueOf(c);
                    if (!g.getFont().canDisplay(c)) {
                        g.setFont(Main.unicodeFont);
                    } else {
                        if (format == 1) {
                            g.setFont(Main.boldFont);
                            fontHeight1 = g.getFontMetrics().getHeight();
                        } else {
                            g.setFont(Main.regularFont);
                            fontHeight1 = g.getFontMetrics().getHeight();
                        }
                    }
                    if (line.equals("&")) {
                        checking = true;
                        continue;
                    }
                    if (checking) {
                        if (ColorCodes.ALL_CODES.contains(line)) {
                            lastColor = check(line, lastColor, g);
                            lastColor2 = check2(line, lastColor2, g);
                            checking = false;
                            continue;
                        }
                        checking = false;
                        g.setColor(lastColor2);
                        g.drawString("&", 10 + h + xoffset, (fontHeight1 + (i) * fontHeight1) + yoffset);
                        g.setColor(lastColor);
                        g.drawString("&", 8 + h + xoffset, (fontHeight1 - 2 + (i) * fontHeight1) + yoffset);
                        h += g.getFontMetrics().stringWidth("&");
                    }
                    g.setColor(lastColor2);
                    g.drawString(format == 2 ? magicChar : line, 10 + h + xoffset, (fontHeight1 + (i) * fontHeight1) + yoffset);
                    g.setColor(lastColor);
                    g.drawString(format == 2 ? magicChar : line, 8 + h + xoffset, ((fontHeight1 - 2) + ((i) * fontHeight1)) + yoffset);
                    h += g.getFontMetrics().stringWidth(line);
                }
                format = 0;
            }
            images.add(firstImage);
        }
        GifSequenceWriter gif = null;
        String url = null;
        System.out.println(images.size());
        try {
            File file = new File(guild + "/" + "Skysmith-" + member + ".gif");
            gif = new GifSequenceWriter(ImageIO.createImageOutputStream(file), BufferedImage.TYPE_INT_ARGB, 20, true);
            for (BufferedImage image : images) gif.writeToSequence(image);
            gif.close();
            try {
                Uploader uploader = new FileUploader(Main.client, file);
                com.uploadcare.api.File file2 = uploader.upload().save();
                url = file2.getOriginalFileUrl().toString();
            } catch (UploadFailureException e) {
                System.out.println("Upload failed :(");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created gif in " + (System.currentTimeMillis() - ms) + "ms");
        return url;
    }

    private Color check(String current, Color lastColor, Graphics2D graphics) {
        for (ColorCodes c : ColorCodes.values()) {
            if (c.toString().equals("&" + current)) {
                switch (c) {
                    case BOLD: {
                        graphics.setFont(Main.boldFont);
                        format = 1;
                        break;
                    }
                    case MAGIC: {
                        format = 2;
                        break;
                    }
                    case RESET: {
                        graphics.setFont(Main.regularFont);
                        format = 0;
                        return Color.WHITE;
                    }
                }
                if (c.hex == null) return lastColor;
                return Color.decode(c.hex);
            }
        }
        return lastColor;
    }

    private Color check2(String current, Color lastColor, Graphics2D graphics) {
        for (ColorCodes c : ColorCodes.values()) {
            if (c.toString().matches("[\uE700-\uE72E\uE730\uE731\uE734\uE735\uE737-\uE756]")) {
                graphics.setFont(Font.getFont("segoe-ui-emoji").deriveFont(22f));
            }
            if (c.toString().equals("&" + current)) {
                switch (c) {
                    case BOLD: {
                        graphics.setFont(Main.boldFont);
                        format = 1;
                        break;
                    }
                    case MAGIC: {
                        format = 2;
                        break;
                    }
                    case RESET: {
                        graphics.setFont(Main.regularFont);
                        format = 0;
                        return Color.decode("#3f3f3f");
                    }
                }
                if (c.shadowHex == null) return lastColor;
                if (c.toString().matches("[\uE700-\uE72E\uE730\uE731\uE734\uE735\uE737-\uE756]")) {
                    format = 3;
                }
                return Color.decode(c.shadowHex);
            }
        }
        return lastColor;
    }
}
