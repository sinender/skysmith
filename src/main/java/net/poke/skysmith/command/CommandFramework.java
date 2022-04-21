package net.poke.skysmith.command;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.poke.skysmith.Main;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandFramework extends ListenerAdapter {
    public final String name;
    public static ArrayList<CommandFramework> commands = new ArrayList<>();

    public CommandFramework(String name, String description, OptionData... options) {
        this.name = name;
        Main.jda.upsertCommand(name, description).addOptions(options).queue();
        Main.jda.addEventListener(this);
    }

    public abstract void run (Member member, Channel channel, List<OptionMapping> options, SlashCommandInteractionEvent event);

    public static void add (CommandFramework command) {
        commands.add(command);
    }
}
