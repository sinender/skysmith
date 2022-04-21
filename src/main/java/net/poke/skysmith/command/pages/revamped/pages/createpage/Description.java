package net.poke.skysmith.command.pages.revamped.pages.createpage;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.pages.CreatePage;
import net.poke.skysmith.command.pages.revamped.pages.description.AddLines;
import net.poke.skysmith.command.pages.revamped.pages.description.RemoveLine;
import net.poke.skysmith.command.pages.revamped.pages.description.SetLines;
import net.poke.skysmith.utils.ButtonColor;

public class Description extends Page {
    public Description() {
        super("Description", 4);
        embedBuilder.setDescription("Welcome to the description editor, here you can edit the lore of the item you are creating!");
        set(new Button() {
            @Override
            public int getSlot() {
                return 0;
            }

            @Override
            public String label() {
                return "Add Line";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("➕");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.GREEN;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new AddLines().open(e);
            }
        });
        set(new Button() {
            @Override
            public int getSlot() {
                return 1;
            }

            @Override
            public String label() {
                return "Remove Line";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("🗑️");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.RED;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new RemoveLine(e.getMember()).open(e);
            }
        });
        set(new Button() {
            @Override
            public int getSlot() {
                return 2;
            }
            @Override
            public String label() {
                return "Set Lines";
            }
            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("📄");
            }
            @Override
            public ButtonColor color() {
                return ButtonColor.BLUE;
            }
            @Override
            public void run(ButtonInteractionEvent e) {
                new SetLines().open(e);
            }
        });
        set(new Button() {
            @Override
            public int getSlot() {
                return 3;
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
                new CreatePage().open(e);
            }
        });
    }
}
