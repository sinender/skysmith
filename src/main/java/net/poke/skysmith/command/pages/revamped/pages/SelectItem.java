package net.poke.skysmith.command.pages.revamped.pages;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Selection;
import net.poke.skysmith.utils.Item;

import java.util.ArrayList;
import java.util.List;

public class SelectItem extends Page {
    public SelectItem(Member member) {
        super("Select Item", 1);
        set(new Selection() {
            @Override
            public List<SelectOption> options() {
                ArrayList<SelectOption> options = new ArrayList<>();
                Item.memberItems.get(member.getId()).forEach(item -> {
                    options.add(SelectOption.of(item.name + "-" + item.id, item.id));
                });
                return options;
            }

            @Override
            public void run(SelectMenuInteractionEvent event, SelectOption selectedOption) {
                Item item = Item.memberItems.get(event.getMember().getId()).stream().filter(i -> i.id.equals(selectedOption.getValue())).findFirst().orElse(null);
                if (item != null) {
                    Item.currentlyEditing.put(event.getMember().getId(), item);
                    new CreatePage().open(event);
                }
            }

            @Override
            public int getSlot() {
                return 0;
            }
        });
    }
}
