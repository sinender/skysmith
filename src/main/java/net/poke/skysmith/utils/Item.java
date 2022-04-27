package net.poke.skysmith.utils;

import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.poke.skysmith.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
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

    private int width () {
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

    private int height (Font font) {
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
        if (!stats.isEmpty()) {
            for (Stat stat : stats.keySet()) {
                lore.add("&7" + stat.name() + ": " + stats.get(stat));
            }
            lore.add("");
        }
        lore.addAll(description);
        lore.add(rarity);
        int width = 14 + width();
        int height = 10 + lore.size() * height(Main.regularFont);
        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();

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
                    System.out.println("Cannot display " + c);
                    graphics.setFont(Main.unicodeFont);
                    yoffset += ((i + 1)*graphics.getFontMetrics().getHeight()) - Math.max(2.5 ,i-13.35)*graphics.getFontMetrics().getHeight();
                } else {
                    graphics.setFont(Main.regularFont);
                }
                if (line.equals("&")) {
                    checking = true;
                    continue;
                }
                if (checking) {
                    if (ColorCodes.ALL_CODES.contains(line)) {
                        lastColor = check(line, lastColor, graphics);
                        lastColor2 = check2(line, lastColor, graphics);
                        checking = false;
                        continue;
                    }
                    checking = false;
                    graphics.setColor(lastColor2);
                    graphics.drawString("&", 10 + h + xoffset, (graphics.getFontMetrics().getHeight() + (i) * graphics.getFontMetrics().getHeight()) + yoffset);
                    graphics.setColor(lastColor);
                    graphics.drawString("&", 8 + h + xoffset, (graphics.getFontMetrics().getHeight() - 2 + (i) * graphics.getFontMetrics().getHeight()) + yoffset);
                    h += graphics.getFontMetrics().stringWidth("&");
                }
                graphics.setColor(lastColor2);
                graphics.drawString(line, 10 + h + xoffset, (graphics.getFontMetrics().getHeight() + (i) * graphics.getFontMetrics().getHeight()) + yoffset);
                graphics.setColor(lastColor);
                graphics.drawString(line, 8 + h + xoffset, (graphics.getFontMetrics().getHeight() - 2 + (i) * graphics.getFontMetrics().getHeight()) + yoffset);
                h += graphics.getFontMetrics().stringWidth(line);
            }
        }

        if (!hasMagic()) {
            System.out.println("Created image in " + (System.currentTimeMillis() - ms) + "ms");
            String url = null;
            try {
                new File(guild).mkdir();
                File file = new File(guild + "/" + "Skysmith-" + member + ".png");
                file.createNewFile();
                ImageIO.write(bufferedImage, "png", file);
                /*try {
                    Uploader uploader = new FileUploader(Main.client, file);
                    com.uploadcare.api.File file2 = uploader.upload().save();
                    url = file2.getOriginalFileUrl().toString();
                } catch (UploadFailureException e) {
                    System.out.println("Upload failed :(");
                }
                System.out.println(url);*/
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

    private int getMagicChars(String str) {
        int amount = 1;
        if (str.contains("&k")) {
            char[] charArray = str.toCharArray();
            boolean checking = false;
            for (int j = 0; j < charArray.length; j++) {
                String line = String.valueOf(charArray[j]);
                if (line.equals("&")) {
                    checking = true;
                    continue;
                }
                if (checking) {
                    if (ColorCodes.ALL_CODES.contains(line)) {
                        formatCheck(line);
                        if (format == 2) amount -= 2;
                    }
                    checking = false;
                }
                if (format == 2) {
                    amount++;
                }
            }
        }
        return amount;
    }

    private String buildMagic(String guild, String member, Long ms) {
        ArrayList<String> thing = new ArrayList<>();
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int e = 0; e < 136; e++) {
            int width = 10 + width();
            int height = 14 + lore.size() * 10;
            BufferedImage bufferedImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bufferedImage.createGraphics();

            graphics.setFont(Main.regularFont);

            graphics.setColor(new Color(44, 8, 99, 255));
            graphics.drawRect(2, 2, width - 5, height - 5);

            graphics.setColor(new Color(85, 85, 85));

            for (int i = 0; i < lore.size(); i++) {
                thing.clear();
                int magic = 0;
                for (int b = 0; b < getMagicChars(lore.get(i)); b++) {
                    thing.add(randomMagic());
                }
                char[] charArray = lore.get(i).toCharArray();
                int h = 0;
                String last = null;
                Color lastColor = Color.decode("#3f3f3f");
                graphics.setFont(Main.regularFont);
                format = 0;
                boolean checking = false;
                String regex = "[\uE700-\uE72E\uE730\uE731\uE734\uE735\uE737-\uE756]";
                Pattern p = Pattern.compile(regex);
                for (char value : charArray) {
                    String line = String.valueOf(value);

                    if (graphics.getFont().canDisplay(value)) {
                        System.out.println("Cannot display " + line);
                    }
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
                        graphics.setColor(lastColor);
                        graphics.drawString(format == 2 ? thing.get(magic) : "&", 7 + h, 15 + (i) * 11);
                        h += graphics.getFontMetrics().stringWidth("&");
                        if (format == 2) magic++;
                    }
                    graphics.setColor(lastColor);
                    graphics.drawString(format == 2 ? thing.get(magic) : line, 7 + h, 15 + (i) * 11);
                    h += graphics.getFontMetrics().stringWidth(line);
                    if (format == 2) magic++;
                }

                h = 0;
                lastColor = Color.WHITE;
                graphics.setFont(Main.regularFont);
                checking = false;
                magic = 0;
                for (char c : charArray) {
                    String line = String.valueOf(c);
                    if (line.equals("&")) {
                        checking = true;
                        continue;
                    }
                    if (checking) {
                        if (ColorCodes.ALL_CODES.contains(line)) {
                            lastColor = check(line, lastColor, graphics);
                            checking = false;
                            continue;
                        }
                        checking = false;
                        graphics.setColor(lastColor);
                        graphics.drawString(format == 2 ? thing.get(magic) : "&", 6 + h, 14 + (i) * 11);
                        h += graphics.getFontMetrics().stringWidth("&");
                        if (format == 2) magic++;
                    }
                    graphics.setColor(lastColor);
                    graphics.drawString(format == 2 ? thing.get(magic) : line, 6 + h, 14 + (i) * 11);
                    h += graphics.getFontMetrics().stringWidth(line);
                    if (format == 2) magic++;
                }
            }
            images.add(bufferedImage);
        }
        System.out.println(images);
        GifSequenceWriter gif = null;
        String url = null;
        try {
            File file = new File(guild + "/" + "Skysmith-" + member + ".png");
            gif = new GifSequenceWriter(ImageIO.createImageOutputStream(file), 1, 20, true);
            for (BufferedImage img : images) {
                gif.writeToSequence(img);
            }
            gif.close();
            /*try {
                Uploader uploader = new FileUploader(Main.client, file);
                com.uploadcare.api.File file2 = uploader.upload().save();
                url = file2.getOriginalFileUrl().toString();
            } catch (UploadFailureException e) {
                System.out.println("Upload failed :(");
            }*/
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

    private void formatCheck(String current) {
        for (ColorCodes c : ColorCodes.values()) {
            if (c.toString().equals("&" + current)) {
                switch (c) {
                    case BOLD: {
                        format = 1;
                        break;
                    }
                    case MAGIC: {
                        format = 2;
                        break;
                    }
                    case RESET: {
                        format = 0;
                    }
                }
            }
        }
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
        if (current.toString().matches("[\uE700-\uE72E\uE730\uE731\uE734\uE735\uE737-\uE756]")) {
            format = 3;
        }
        return lastColor;
    }

    public static String removeColor(String line) {
        StringBuilder sb = new StringBuilder();
        char[] charArray = line.toCharArray();
        boolean checking = false;
        for (char c : charArray) {
            String l = String.valueOf(c);
            if (l.equals("&")) {
                checking = true;
                continue;
            }
            if (checking) {
                if (ColorCodes.ALL_CODES.contains(l)) {
                    checking = false;
                    continue;
                }
                sb.append("&");
                checking = false;
            }
            sb.append(l);
        }
        return sb.toString();
    }
}
