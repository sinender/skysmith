package net.poke.skysmith.utils;

import net.dv8tion.jda.api.entities.Emoji;

public enum Stat {
    DAMAGE(Emoji.fromUnicode("⚔"), ""),
    SPEED("&a", Emoji.fromUnicode("💨"), ""),
    HEALTH("&a", Emoji.fromUnicode("❤"), ""),
    DEFENSE("&a", Emoji.fromUnicode("❇"), ""),
    INTELLIGENCE("&a", Emoji.fromUnicode("✏"), ""),
    CRITICAL_CHANCE(Emoji.fromUnicode("☣"), "%"),
    CRITICAL_DAMAGE(Emoji.fromUnicode("☠"), "%"),
    STRENGTH(Emoji.fromUnicode("💪"), ""),
    ;

    public Emoji emoji;
    public String suffix;
    public String color;
    Stat(Emoji emoji, String suffix) {
        this.emoji = emoji;
        this.suffix = suffix;
        this.color = "&c";
    }
    Stat(String color, Emoji emoji, String suffix) {
        this.emoji = emoji;
        this.suffix = suffix;
        this.color = color;
    }

}
