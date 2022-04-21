package net.poke.skysmith.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (CommandFramework command : CommandFramework.commands) {
            if (command.name.equals(event.getName())) {
                command.run(event.getMember(), event.getChannel(), event.getOptions(), event);
            }
        }
    }
}
