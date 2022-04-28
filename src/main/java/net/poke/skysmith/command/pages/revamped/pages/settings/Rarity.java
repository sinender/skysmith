package net.poke.skysmith.command.pages.revamped.pages.settings;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.command.pages.revamped.interactions.Selection;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Settings;
import net.poke.skysmith.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Rarity extends Page {
    public Rarity() {
        super("Rarity", 2);
        embedBuilder.setDescription("Use one of the options to set the rarity of the item, or you can choose to create a custom one.");
        set(new Selection() {
            @Override
            public List<SelectOption> options() {
                List<SelectOption> options = new ArrayList<>();
                options.add(SelectOption.of("Back", "Back").withEmoji(Emoji.fromUnicode("⬅")));
                for (net.poke.skysmith.utils.Rarity rarity : net.poke.skysmith.utils.Rarity.values()) {
                    options.add(SelectOption.of(rarity.name(), rarity.name()));
                }
                options.add(SelectOption.of("Custom", "Custom").withDescription("Set a custom rarity for your item.").withEmoji(Emoji.fromUnicode("💡")));
                return options;
            }

            @Override
            public void run(SelectMenuInteractionEvent event, SelectOption selectedOption) {
                if (selectedOption.getLabel().equals("Back")) {
                    new Settings().open(event);
                    return;
                }
                if (selectedOption.getLabel().equals("Custom")) {
                    embedBuilder.setDescription("Please reply with the custom rarity of your item.");
                    return;
                }
                event.getChannel().sendMessage("Rarity set to " + selectedOption.getLabel() + ".").queue(message -> {
                    message.delete().queueAfter(5, TimeUnit.SECONDS);
                });
                Item.currentlyEditing.get(event.getMember().getId()).rarity = net.poke.skysmith.utils.Rarity.valueOf(selectedOption.getLabel()).color + net.poke.skysmith.utils.Rarity.valueOf(selectedOption.getLabel()).displayName;
                new Rarity().open(event);
            }

            @Override
            public int getSlot() {
                return 0;
            }
        });
        set(new Message() {
            @Override
            public void run(MessageReceivedEvent e) {
                e.getMessage().reply("Rarity set to " + e.getMessage().getContentRaw() + ".").queue();
                Item.currentlyEditing.get(e.getMember().getId()).rarity = e.getMessage().getContentRaw();
                new Rarity().open(e);
            }

            @Override
            public int getSlot() {
                return 1;
            }
        });
    }
}
