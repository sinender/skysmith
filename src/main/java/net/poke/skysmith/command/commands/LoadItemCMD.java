package net.poke.skysmith.command.commands;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.poke.skysmith.Main;
import net.poke.skysmith.command.CommandFramework;
import net.poke.skysmith.command.pages.revamped.pages.CreatePage;
import net.poke.skysmith.utils.Item;
import net.poke.skysmith.utils.Stat;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoadItemCMD extends CommandFramework {
    public LoadItemCMD() {
        super("loaditem", "Loads an item from the database.", new OptionData(OptionType.STRING, "id", "The id of the item.", true).addChoices(getChoices()));
    }

    private static List<Command.Choice> getChoices() {
        List<Command.Choice> choices = new ArrayList<>();
        for (Document doc : Main.mongo.getAllDocuments()) {
            choices.add(new Command.Choice(doc.getString("_id"), doc.getString("_id")));
        }
        return choices;
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        event.replyChoices(getChoices()).queue();
    }

    @Override
    public void run(Member member, Channel channel, List<OptionMapping> options, SlashCommandInteractionEvent event) {
        String id = options.get(0).getAsString();
        Document doc = Main.mongo.getDoc(id);
        if (doc == null) {
            event.reply("Item not found.").setEphemeral(true).queue();
        }
        Item item = new Item(id, doc.getString("name"), doc.getList("description", String.class), doc.getString("rarity"));
        item.pureLore = doc.getBoolean("pureLore");
        item.numberedLore = doc.getBoolean("numberedLore");
        Document stats = doc.get("stats", Document.class);
        for (Stat stat : Stat.values()) {
            if (stats.containsKey(stat.name())) {
                item.stats.put(stat, stats.getInteger(stat.name()));
            }
        }
        ArrayList<Item> items = Item.memberItems.containsKey(member.getId()) ? Item.memberItems.get(member.getId()) : new ArrayList<>();
        items.add(item);
        Item.memberItems.put(member.getId(), items);
        Item.currentlyEditing.put(member.getId(), item);
        new CreatePage().open(event);
    }
}
