package net.poke.skysmith.utils;

import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;

public enum Rarity {
    COMMON(ColorCodes.WHITE,"COMMON"),
    UNCOMMON(ColorCodes.GREEN,"UNCOMMON"),
    RARE(ColorCodes.BLUE,"RARE"),
    EPIC(ColorCodes.DARK_PURPLE,"EPIC"),
    LEGENDARY(ColorCodes.GOLD,"LEGENDARY"),
    MYTHIC(ColorCodes.LIGHT_PURPLE,"MYTHIC"),
    SPECIAL(ColorCodes.RED,"SPECIAL"),
    SUPER_SPECIAL(ColorCodes.RED,"SUPER SPECIAL"),
    SUPREME(ColorCodes.DARK_RED,"SUPREME"),
    DIVINE(ColorCodes.AQUA, "DIVINE");

    private final ColorCodes color;
    private final String displayName;

    Rarity(ColorCodes color, String displayName) {
        this.color = color;
        this.displayName = displayName;
    }

    public static List<Command.Choice> rarities () {
        List<Command.Choice> rarities = new ArrayList<>();
        for (Rarity r : Rarity.values()) {
            rarities.add(new Command.Choice(r.displayName, r.color + r.displayName));
        }
        return rarities;
    }
}
