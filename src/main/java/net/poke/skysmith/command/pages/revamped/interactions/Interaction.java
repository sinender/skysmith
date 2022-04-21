package net.poke.skysmith.command.pages.revamped.interactions;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import net.poke.skysmith.utils.ButtonColor;

public interface Interaction {
    int getSlot();

    static ButtonImpl none () {
        return new ButtonImpl("null", "", ButtonStyle.UNKNOWN, true, null);
    }
    static Button none2 (int slot) {
        return new Button() {
            @Override
            public String label() {
                return "";
            }

            @Override
            public Emoji emoji() {
                return null;
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.GREY;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                e.reply("This button does nothing.").setEphemeral(true);
            }

            @Override
            public int getSlot() {
                return slot;
            }
        };
    }
}
