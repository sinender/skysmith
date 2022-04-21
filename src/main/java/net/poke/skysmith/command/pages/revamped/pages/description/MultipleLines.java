package net.poke.skysmith.command.pages.revamped.pages.description;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.utils.ButtonColor;
import net.poke.skysmith.utils.Item;
import net.poke.skysmith.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MultipleLines extends Page {
    public MultipleLines() {
        super("Multiple Lines", 2);
        embedBuilder.setDescription("Welcome to the multi line setter, you can reply to this message with two formats\n\n<line number> - <lore line>\n<line number> - <lore line>\n\nor\n\n<lore line>\n<lore line>\n\nNOTE: The second format will replace your entire description!");

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
                new SetLines().open(e);
            }

            @Override
            public int getSlot() {
                return 0;
            }
        });
        set(new Message() {
            @Override
            public void run(MessageReceivedEvent event) {
                Item item = Item.currentlyEditing.get(event.getMember().getId());
                boolean hasint = false;
                HashMap<Integer, String> newLore = new HashMap<>();
                String[] split = event.getMessage().getContentDisplay().split("\n");
                for (String s : split) {
                    if (!Utils.isInteger(s.split(" - ")[0]) && hasint) {
                        event.getMessage().reply("Please follow one of the formats provided.").queue(msg -> {
                            msg.delete().queueAfter(5, TimeUnit.SECONDS);
                            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        return;
                    }
                    if (Utils.isInteger(s.split(" - ")[0])) {
                        if (Integer.parseInt(s.split(" - ")[0]) <= 0) {
                            event.getMessage().reply("The input must be greater than 0.").queue(msg -> {
                                msg.delete().queueAfter(5, TimeUnit.SECONDS);
                                event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                            });
                            return;
                        }
                        hasint = true;
                    }
                }
                for (int i = 0; i < split.length; i++) {
                    if (!hasint) {
                        newLore.put(i, split[i]);
                    } else {
                        newLore.put(Integer.parseInt(split[i].split(" - ")[0]) - 1, split[i].split(" - ")[1]);
                    }
                }
                if (!hasint) item.description.clear();
                for (Map.Entry<Integer, String> entry : newLore.entrySet()) {
                    Integer key = entry.getKey();
                    String val = entry.getValue();
                    item.setLine(key, val);
                }
                event.getMessage().delete().queue();
                new MultipleLines().open(event);
            }

            @Override
            public int getSlot() {
                return 1;
            }
        });
    }
}
