package net.poke.skysmith.command.pages.revamped.pages.description;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Description;
import net.poke.skysmith.utils.ButtonColor;

public class SetLines extends Page {
    public SetLines() {
        super("Set Lines", 3);
        embedBuilder.setDescription("To set a line on the item, choose from singular line editing or multi line editing!");
        set(new Button() {
            @Override
            public String label() {
                return "One Line";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("1️⃣");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.GREEN;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new OneLine(e.getMember(), null).open(e);
            }

            @Override
            public int getSlot() {
                return 0;
            }
        });
        set(new Button() {
            @Override
            public int getSlot() {
                return 1;
            }

            @Override
            public String label() {
                return "Multi Lines";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("🔢");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.BLUE;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new MultipleLines().open(e);
            }
        });
        set(new Button() {
            @Override
            public int getSlot() {
                return 2;
            }

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
        });
    }
}
