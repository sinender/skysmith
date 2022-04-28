package net.poke.skysmith.command.pages.revamped.interactions;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.List;

public interface Selection extends Interaction {
    List<SelectOption> options();
    void run (SelectMenuInteractionEvent event, SelectOption selectedOption);
}
