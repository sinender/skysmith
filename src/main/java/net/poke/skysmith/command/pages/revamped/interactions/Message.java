package net.poke.skysmith.command.pages.revamped.interactions;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Message extends Interaction {
    void run(MessageReceivedEvent e);
}
