package net.poke.skysmith.utils;

import net.dv8tion.jda.api.entities.Emoji;

public enum Stat {
    DAMAGE("Damage", Emoji.fromUnicode("⚔"), ""),
    SPEED("Speed", "&a", Emoji.fromUnicode("💨"), ""),
    HEALTH("Health", "&a", Emoji.fromUnicode("❤"), ""),
    DEFENSE("Defense", "&a", Emoji.fromUnicode("❇"), ""),
    INTELLIGENCE("Intelligence", "&a", Emoji.fromUnicode("✏"), ""),
    CRITICAL_CHANCE("Critical Chance", Emoji.fromUnicode("☣"), "%"),
    CRITICAL_DAMAGE("Critical Damage", Emoji.fromUnicode("☠"), "%"),
    STRENGTH("Strength", Emoji.fromUnicode("💪"), ""),
    ;

    public Emoji emoji;
    public String displayName;
    public String suffix;
    public String color;
    Stat(String displayName, Emoji emoji, String suffix) {
        this.emoji = emoji;
        this.displayName = displayName;
        this.suffix = suffix;
        this.color = "&c";
    }
    Stat(String displayName, String color, Emoji emoji, String suffix) {
        this.emoji = emoji;
        this.displayName = displayName;
        this.suffix = suffix;
        this.color = color;
    }

}
