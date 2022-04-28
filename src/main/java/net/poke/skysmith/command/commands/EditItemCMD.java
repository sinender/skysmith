package net.poke.skysmith.command.commands;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.poke.skysmith.command.CommandFramework;
import net.poke.skysmith.command.pages.revamped.pages.CreatePage;
import net.poke.skysmith.command.pages.revamped.pages.SelectItem;
import net.poke.skysmith.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditItemCMD extends CommandFramework {
    public EditItemCMD(String memberId) {
        super("edititem", "Edit an item that you have created.");
    }

    @Override
    public void run(Member member, Channel channel, List<OptionMapping> options, SlashCommandInteractionEvent event) {
        if (Item.memberItems.containsKey(member.getId())) {
            new SelectItem(member).open(event);
        } else {
            event.reply("You have no items to edit.").setEphemeral(true).queue();
        }
    }
}
