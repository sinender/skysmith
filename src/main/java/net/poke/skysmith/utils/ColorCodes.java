package net.poke.skysmith.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum ColorCodes {
    BLACK('0', "black", "#000000", "#000000"),
    DARK_BLUE('1', "dark_blue", "#0000AA", "#00002a"),
    DARK_GREEN('2', "dark_green", "#00AA00", "#002a00"),
    DARK_AQUA('3', "dark_aqua", "#00AAAA", "#002a2a"),
    DARK_RED('4', "dark_red", "#AA0000", "#2a0000"),
    DARK_PURPLE('5', "dark_purple", "#AA00AA", "#2a002a"),
    GOLD('6', "gold", "#FFAA00", "#2a2a00"),
    GRAY('7', "gray", "#AAAAAA", "#2a2a2a"),
    DARK_GRAY('8', "dark_gray", "#555555", "#151515"),
    BLUE('9', "blue", "#5555FF", "#15153f"),
    GREEN('a', "green", "#55FF55", "#153f15"),
    AQUA('b', "aqua", "#55FFFF", "#153f3f"),
    RED('c', "red", "#FF5555", "#3f1515"),
    LIGHT_PURPLE('d', "light_purple", "#FF55FF", "#3f153f"),
    YELLOW('e', "yellow", "#FFFF55", "#3f3f15"),
    WHITE('f', "white", "#FFFFFF", "#3f3f3f"),
    MAGIC('k', "obfuscated", null, null),
    BOLD('l', "bold", null, null),
    STRIKETHROUGH('m', "strikethrough", null, null),
    UNDERLINE('n', "underline", null, null),
    RESET('r', "reset", null, null);

    public static final char COLOR_CHAR = '§';
    public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnRr";
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
    private static final Map<Character, ColorCodes> BY_CHAR = new HashMap();
    private final char code;
    private final String toString;
    private final String name;
    public final String hex;
    public final String shadowHex;

    private ColorCodes(char code, String name, String hex, String shadowHex) {
        this.code = code;
        this.name = name;
        this.hex = hex;
        this.shadowHex = shadowHex;
        this.toString = new String(new char[]{'&', code});
    }

    public String toString() {
        return this.toString;
    }
}
