package net.poke.skysmith.command.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import net.poke.skysmith.Main;
import net.poke.skysmith.command.CommandFramework;
import net.poke.skysmith.command.pages.revamped.pages.CreatePage;
import net.poke.skysmith.utils.Item;
import net.poke.skysmith.utils.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CreateCMD extends CommandFramework {
    public CreateCMD() {
        super("create", "Main command for Skysmith.");
    }

    @Override
    public void run(Member member, Channel channel, List<OptionMapping> options, SlashCommandInteractionEvent event) {
        String id = UUID.randomUUID().toString();
        Item item = new Item(id, "Editable Item", new ArrayList<>(Arrays.asList("", "This item is editable! Use", "the buttons below to", "start editing", "")), "&6LEGENDARY");
        ArrayList<Item> items = Item.memberItems.get(member.getId()) == null ? new ArrayList<>() : Item.memberItems.get(member.getId());
        items.add(item);
        Item.memberItems.put(member.getId(), items);
        Item.currentlyEditing.put(member.getId(), item);
        new CreatePage().open(event);
    }
}
