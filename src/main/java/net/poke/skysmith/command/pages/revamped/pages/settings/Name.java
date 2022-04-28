package net.poke.skysmith.command.pages.revamped.pages.settings;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Settings;
import net.poke.skysmith.utils.ButtonColor;
import net.poke.skysmith.utils.Item;

import java.util.concurrent.TimeUnit;

public class Name extends Page {
    public Name() {
        super("Name", 2);
        embedBuilder.setDescription("Reply to this message with the name of the item you want to create.");
        set(new Button() {
            @Override
            public String label() {
                return "Back";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("⬅");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.BLUE;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new Settings().open(e);
            }

            @Override
            public int getSlot() {
                return 0;
            }
        });
        set(new Message() {
            @Override
            public void run(MessageReceivedEvent e) {
                Item.currentlyEditing.get(e.getMember().getId()).name = e.getMessage().getContentDisplay();
                e.getMessage().reply("Successfully set name to " + Item.currentlyEditing.get(e.getMember().getId()).name).queue(message -> {
                    message.delete().queueAfter(5, TimeUnit.SECONDS);
                    e.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                });
                new Name().open(e);
            }

            @Override
            public int getSlot() {
                return 1;
            }
        });
    }
}
