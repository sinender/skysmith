package net.poke.skysmith.command.pages.revamped.pages;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Description;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Finish;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Settings;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Stats;
import net.poke.skysmith.utils.ButtonColor;
import net.poke.skysmith.utils.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static net.poke.skysmith.Main.mongo;

public class CreatePage extends Page {
    public CreatePage() {
        super("Skysmith Item Creator", 5);
        embedBuilder.setDescription("Welcome to the Skysmith interactive item creator!\n You can create an item using any of the below options!");
        set(new Button() {
            @Override
            public String label() {
                return "Description";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("📒");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.GREEN;
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
        set(new Button() {
            @Override
            public int getSlot() {
                return 1;
            }

            @Override
            public String label() {
                return "Statistic Modifier";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("🗡");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.RED;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new Stats(e.getMember()).open(e);
            }
        });
        set(new Button() {

            @Override
            public int getSlot() {
                return 2;
            }

            @Override
            public String label() {
                return "Settings";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("⚙");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.BLUE;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new Settings().open(e);
            }
        });
        set(new Button() {
            @Override
            public int getSlot() {
                return 3;
            }

            @Override
            public String label() {
                return "Ability Editor";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("📜");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.GREY;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                //todo add the ability editor page
                e.reply("This feature is not yet implemented!").setEphemeral(true).queue();
            }
        });
        set(new Button() {
            @Override
            public int getSlot() {
                return 4;
            }

            @Override
            public String label() {
                return "Finish";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("💾");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.GREEN;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                new Finish(e.getMember()).open(e);
                if (Item.memberItems.containsKey(e.getMember().getId())) {
                    ArrayList<Item> items = new ArrayList<>(Item.memberItems.get(e.getMember().getId()));
                    items.add(Item.currentlyEditing.get(e.getMember().getId()));
                    Item.memberItems.put(e.getMember().getId(), items);
                } else {
                    ArrayList<Item> items = new ArrayList<>();
                    items.add(Item.currentlyEditing.get(e.getMember().getId()));
                    Item.memberItems.put(e.getMember().getId(), items);
                }
                mongo.saveItem(Item.currentlyEditing.get(e.getMember().getId()), e.getMember().getId());
            }
        });
    }
}
