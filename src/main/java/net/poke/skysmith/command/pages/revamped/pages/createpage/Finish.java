package net.poke.skysmith.command.pages.revamped.pages.createpage;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.AfterOpen;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.pages.CreatePage;
import net.poke.skysmith.utils.ButtonColor;
import net.poke.skysmith.utils.Item;

public class Finish extends Page {
    public Finish(Member member) {
        super("Completed Item", 2);
        embedBuilder.setDescription("Thank You for using SkySmith Item Creator. If the image below is shrinked down, that means discord has compressed your image. Click on the image and click open original to see the full image. I suggest joining our discord server: https://discord.gg/38CatS7smV");
        if (member != null) {
            embedBuilder.addField("ID", Item.currentlyEditing.get(member.getId()).id, true);
            embedBuilder.setImage(Item.currentlyEditing.get(member.getId()).build(member.getGuild(), member));
        }
        set(new Button() {
            @Override
            public String label() {
                return "Edit";
            }

            @Override
            public Emoji emoji() {
                return Emoji.fromUnicode("✏");
            }

            @Override
            public ButtonColor color() {
                return ButtonColor.BLUE;
            }

            @Override
            public void run(ButtonInteractionEvent e) {
                Item.currentlyEditing.put(e.getMember().getId(), Item.getItem(e.getMember().getId(), e.getMessage().getEmbeds().get(0).getFields().get(0).getValue()));
                new CreatePage().open(e);
            }

            @Override
            public int getSlot() {
                return 0;
            }
        });
        set(new AfterOpen() {
            @Override
            public void run(Member member) {
                Item.currentlyEditing.remove(member.getId());
            }

            @Override
            public int getSlot() {
                return 1;
            }
        });
    }
}
