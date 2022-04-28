package net.poke.skysmith.command.pages.revamped.pages.createpage;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.command.pages.revamped.interactions.Selection;
import net.poke.skysmith.command.pages.revamped.pages.CreatePage;
import net.poke.skysmith.command.pages.revamped.pages.settings.Name;
import net.poke.skysmith.command.pages.revamped.pages.settings.Rarity;

import java.util.ArrayList;
import java.util.List;

public class Settings extends Page {
    public Settings() {
        super("Settings", 1);
        embedBuilder.setDescription("Please select a setting to change.");
        set(new Selection() {
            @Override
            public int getSlot() {
                return 0;
            }

            @Override
            public List<SelectOption> options() {
                List<SelectOption> options = new ArrayList<>();
                options.add(SelectOption.of("Back", "Back").withEmoji(Emoji.fromUnicode("⬅")));
                options.add(SelectOption.of("Name", "name").withDescription("Set the items display name!").withEmoji(Emoji.fromUnicode("✉")));
                options.add(SelectOption.of("Rarity", "rarity").withDescription("Set the items rarity!"));
                options.add(SelectOption.of("Type", "type").withDescription("Set the items type!").withEmoji(Emoji.fromUnicode("🔧")));
                options.add(SelectOption.of("Pure Lore", "pure lore").withDescription("Enable or disable pure lore!").withEmoji(Emoji.fromUnicode("📖")));
                options.add(SelectOption.of("Numbered Lore", "numbered lore").withDescription("Enable or disable numbered lore!").withEmoji(Emoji.fromUnicode("🔢")));
                return options;
            }

            @Override
            public void run(SelectMenuInteractionEvent event, SelectOption selectOption) {
                if (selectOption.getLabel().equals("Back")) {
                    new CreatePage().open(event);
                }
                switch (selectOption.getLabel()) {

                    case "Name": {
                        new Name().open(event);
                    }
                    break;
                    case "Rarity": {
                        new Rarity().open(event);
                    }
                    break;
                    default: {
                        event.reply("The " + selectOption.getLabel() + " has not been implemented yet!").setEphemeral(true).queue();
                    }
                }
            }
        });
    }
}
