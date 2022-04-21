package net.poke.skysmith.command.pages.revamped.pages.description;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Description;
import net.poke.skysmith.utils.ButtonColor;
import net.poke.skysmith.utils.Item;

public class AddLines extends Page {
    public AddLines() {
        super("Add Line", 2);
        embedBuilder.setDescription("To add a line to your items description, reply to this message with your new line! Note it must be under 200 characters!");
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
                return ButtonColor.GREY;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new Description().open(e);
            }

            @Override
            public int getSlot() {
                return 0;
            }
        });
        set(new Message() {
            @Override
            public int getSlot() {
                return 1;
            }

            @Override
            public void run(MessageReceivedEvent e) {
                Item item = Item.currentlyEditing.get(e.getMember().getId());
                item.addLine(e.getMessage().getContentDisplay());
                e.getMessage().delete().queue();
                new AddLines().open(e);
            }
        });
    }
}
