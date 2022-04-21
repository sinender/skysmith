package net.poke.skysmith.command.pages.revamped.pages.createpage;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.command.pages.revamped.interactions.Selection;
import net.poke.skysmith.command.pages.revamped.pages.CreatePage;
import net.poke.skysmith.utils.Item;
import net.poke.skysmith.utils.Stat;
import net.poke.skysmith.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Stats extends Page {
    public static HashMap<String, String> selection = new HashMap<>();
    public Stats(Member member) {
        super("Statistics Modifier", 2);
        if (member != null && selection.containsKey(member.getId())) {
            embedBuilder.setDescription("Reply to this message with the amount of " + selection.get(member.getId()) + " you want to add to your item.");
            embedBuilder.addField("Selected Stat", selection.get(member.getId()), false);
        } else {
            embedBuilder.setDescription("Select the stat you want to modify.");
        }
        set(new Selection() {
            @Override
            public List<SelectOption> options() {
                List<SelectOption> options = new ArrayList<>();
                options.add(SelectOption.of("Back", "Back").withEmoji(Emoji.fromUnicode("⬅")));
                for (Stat stat : Stat.values()) {
                    options.add(SelectOption.of(stat.name(), stat.name()).withEmoji(stat.emoji));
                }
                return options;
            }

            @Override
            public void run(SelectMenuInteractionEvent event) {
                if (event.getInteraction().getSelectedOptions().get(0).getValue().equals("Back")) {
                    new CreatePage().open(event);
                } else {
                    selection.put(event.getMember().getId(), event.getInteraction().getSelectedOptions().get(0).getValue());
                    new Stats(event.getMember()).open(event);
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
                if (!Utils.isInteger(e.getMessage().getContentRaw()) || Integer.parseInt(e.getMessage().getContentRaw()) < 0) {
                    e.getMessage().reply("Please enter a valid number, must be greater than or equal to 0.").queue(msg -> {
                        msg.delete().queueAfter(5, TimeUnit.SECONDS);
                        e.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                } else {
                    if (selection.containsKey(e.getMember().getId())) {
                        Item.currentlyEditing.get(e.getMember().getId()).stats.put(Stat.valueOf(selection.get(e.getMember().getId())), Integer.parseInt(e.getMessage().getContentRaw()));
                        selection.remove(e.getMember().getId());
                        e.getMessage().delete().queue();
                        new Stats(e.getMember()).open(e);
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
