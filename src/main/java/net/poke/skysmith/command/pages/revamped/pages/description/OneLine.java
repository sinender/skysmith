package net.poke.skysmith.command.pages.revamped.pages.description;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.command.pages.revamped.interactions.Selection;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Description;
import net.poke.skysmith.utils.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OneLine extends Page {
    public static HashMap<String, String> selectionMap = new HashMap<>();
    public OneLine(Member member, String selection) {
        super("One Line", 2);
        embedBuilder.setDescription("Choose which line you want to set or you can reply to this message with the format as follows\n<line number> - <lore line>");
        if (selection != null) {
            embedBuilder.addField("Current Selection", selection, false);
        }
        if (member != null) {
            selectionMap.put(member.getId(), selection);
        }
        set(new Selection() {
            @Override
            public List<SelectOption> options() {
                List<SelectOption> options = new ArrayList<>();
                options.add(SelectOption.of("Back", "Back").withEmoji(Emoji.fromUnicode("⬅")));
                for (int i = 0; i < Item.currentlyEditing.get(member.getId()).description.size(); i++) {
                    options.add(SelectOption.of("Line " + (i + 1), String.valueOf(i)).withDescription(Item.currentlyEditing.get(member.getId()).description.get(i)));
                }
                return options;
            }

            @Override
            public void run(SelectMenuInteractionEvent event, SelectOption selectOption) {
                if (event.getInteraction().getSelectedOptions().get(0).getLabel().equals("Back")) {
                    new SetLines().open(event);
                } else {
                    new OneLine(event.getMember(), event.getInteraction().getSelectedOptions().get(0).getLabel()).open(event);
                }
            }

            @Override
            public int getSlot() {
                return 0;
            }
        });
        set(new Message() {
            @Override
            public void run(MessageReceivedEvent e) {
                if (e.getMessage().getContentRaw().contains(" - ")) {
                    String[] split = e.getMessage().getContentRaw().split(" - ");
                    Item.currentlyEditing.get(e.getMember().getId()).description.set(Integer.parseInt(split[0].trim()) - 1, split[1].trim());
                    e.getMessage().delete().queue();
                    new OneLine(e.getMember(), null).open(e);
                } else {
                    if (selectionMap.containsKey(e.getMember().getId())) {
                        Item.currentlyEditing.get(e.getMember().getId()).description.set(Integer.parseInt(selectionMap.get(e.getMember().getId()).replace("Line ", "")) - 1, e.getMessage().getContentRaw());
                        selectionMap.remove(e.getMember().getId());
                        e.getMessage().delete().queue();
                        new OneLine(e.getMember(), null).open(e);
                    } else {
                        e.getMessage().reply("Invalid format. Please use the format as follows\n<line number> - <lore line>").queue(msg -> {
                            msg.delete().queueAfter(5, TimeUnit.SECONDS);
                            e.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                    }
                }
            }

            @Override
            public int getSlot() {
                return 1;
            }
        });
    }
}
