package net.poke.skysmith.command.pages.revamped.interactions;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.poke.skysmith.utils.ButtonColor;

public interface Button extends Interaction {
    String label();
    Emoji emoji();
    ButtonColor color();
    void run(ButtonInteractionEvent e);
}
