package net.poke.skysmith.command.pages.revamped.pages.createpage;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.utils.ButtonColor;

public class Settings extends Page {
    public Settings() {
        super("Settings", 5);
        embedBuilder.setDescription("You can use any of the below settings to customize your item!");
        set(new Button() {
            @Override
            public int getSlot() {
                return 0;
            }

            @Override
            public String label() {
                return "Name";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("✉");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.GREEN;
            }

            @Override
            public void run(ButtonInteractionEvent e) {

            }
        });
    }
}
