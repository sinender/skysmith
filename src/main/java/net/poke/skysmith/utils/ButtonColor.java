package net.poke.skysmith.utils;

import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public enum ButtonColor {
    GREEN (ButtonStyle.SUCCESS),
    BLUE (ButtonStyle.PRIMARY),
    RED (ButtonStyle.DANGER),
    GREY (ButtonStyle.SECONDARY);

    private final ButtonStyle style;

    ButtonColor(ButtonStyle style) {
        this.style = style;
    }

    public ButtonStyle getStyle() {
        return style;
    }
}
