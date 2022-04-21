package net.poke.skysmith.command.pages.revamped.pages.description;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.command.pages.revamped.interactions.Selection;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Description;
import net.poke.skysmith.utils.ButtonColor;
import net.poke.skysmith.utils.Item;

import java.util.ArrayList;
import java.util.List;

public class RemoveLine extends Page {
    public RemoveLine(Member member) {
        super("Remove Line", 2);
        embedBuilder.setDescription("To remove a line from your items description, reply to this message with the number of the line you want to remove!");
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
            public void run(SelectMenuInteractionEvent event) {
                if (event.getInteraction().getSelectedOptions().get(0).getLabel().equals("Back")) {
                    new Description().open(event);
                } else {
                    Item.currentlyEditing.get(event.getMember().getId()).description.remove(Integer.parseInt(event.getInteraction().getSelectedOptions().get(0).getValue()));
                    new RemoveLine(event.getMember()).open(event);
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
                Item item = Item.currentlyEditing.get(e.getMember().getId());
                item.removeLine(Integer.parseInt(e.getMessage().getContentDisplay()) - 1);
                e.getMessage().delete().queue();
                new RemoveLine(e.getMember()).open(e);
            }

            @Override
            public int getSlot() {
                return 1;
            }
        });
    }
}
