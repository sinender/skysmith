package net.poke.skysmith.command.pages.revamped;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import net.dv8tion.jda.internal.interactions.component.ButtonInteractionImpl;
import net.dv8tion.jda.internal.interactions.component.SelectMenuImpl;
import net.poke.skysmith.command.pages.revamped.interactions.AfterOpen;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.interactions.Interaction;
import net.poke.skysmith.command.pages.revamped.interactions.Selection;
import net.poke.skysmith.utils.Item;
import net.poke.skysmith.utils.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Page {
    @Getter
    @Setter
    protected String title;
    @Getter
    protected int size;
    @Getter
    protected EmbedBuilder embedBuilder;
    @Getter
    protected List<Interaction> items;
    @Getter
    protected Item item;
    @Getter
    protected Member member;

    public Page (String title, int size) {
        if (size > 5) {
            throw new IllegalArgumentException("Page size cannot be greater than 5");
        }
        this.title = title;
        this.items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            items.add(Interaction.none2(i));
        }
        this.size = size;
        this.embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);

    }

    public void set(Interaction a) {
        items.removeIf(i -> i.getSlot() == a.getSlot());
        items.add(a);
    }

    public Interaction get(int slot) {
        for (Interaction item : items) {
            if (item.getSlot() == slot)
                return item;
        }
        return null;
    }

    public void open (GenericInteractionCreateEvent event) {
        if (items.size() > size) {
            throw new IllegalArgumentException("Item amount cannot be more than " + size);
        }
        List<ItemComponent> components = actualOpen(event.getMember());

        if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent e = (SlashCommandInteractionEvent) event;
            e.replyEmbeds(embedBuilder.build()).addActionRow(components).queue(message -> {
                new Thread(() -> {
                    embedBuilder.setImage(item.build(member.getGuild().getId(), member.getId()));
                    message.editOriginalEmbeds(embedBuilder.build()).setActionRow(components).queue();
                }).start();
            });
        } else if (event instanceof GenericComponentInteractionCreateEvent) {
            GenericComponentInteractionCreateEvent e = (GenericComponentInteractionCreateEvent) event;
            e.getMessage().editMessageEmbeds(embedBuilder.build()).setActionRow(components).queue();
            new Thread(() -> {
                embedBuilder.setImage(item.build(member.getGuild().getId(), member.getId()));
                e.getMessage().editMessageEmbeds(embedBuilder.build()).setActionRow(components).queue();
            }).start();
            e.deferEdit().queue();
        } else throw new IllegalArgumentException("Event must be SlashCommandInteractionEvent or GenericComponentInteractionCreateEvent");

    }
    public void open (MessageReceivedEvent event) {
        List<ItemComponent> e = actualOpen(event.getMember());
        event.getMessage().getReferencedMessage().editMessageEmbeds(embedBuilder.build()).setActionRow(e).queue();
        new Thread(() -> {
            embedBuilder.setImage(item.build(member.getGuild().getId(), member.getId()));
            event.getMessage().getReferencedMessage().editMessageEmbeds(embedBuilder.build()).setActionRow(e).queue();
        }).start();
        for (Interaction interaction : items) {
            if (interaction instanceof AfterOpen) {
                ((AfterOpen) interaction).run(event.getMember());
            }
        }
    }

    private ArrayList<ItemComponent> actualOpen(Member member) {
        if (items.size() > size) {
            throw new IllegalArgumentException("Item amount cannot be more than " + size);
        }

        this.item = Item.currentlyEditing.get(member.getId());
        List<String> lore = item.getLore();
        embedBuilder.addField("Lore", lore.size() <= 50 ? String.join("\n", lore) : String.join("\n", lore.subList(0, 50)), false);

        ArrayList<ItemComponent> components = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            components.add(Interaction.none());
        }
        boolean hasSelection = false;
        for (Interaction interaction : items) {
            if (interaction instanceof Selection) {
                Selection selection = (Selection) interaction;
                components.set(0, new SelectMenuImpl(title + "-0", "Choose one", 1, 1, false, selection.options()));
                hasSelection = true;
            } else {
                if (hasSelection)
                    components.remove(interaction.getSlot());
            }
        }
        if (!hasSelection) {
            for (Interaction interaction : items) {
                if (interaction instanceof Button) {
                    Button button = (Button) interaction;
                    components.set(button.getSlot(), new ButtonImpl(title + "-" + button.getSlot(), button.label(), button.color().getStyle(), false, button.emoji()));
                } else {
                    components.remove(interaction.getSlot());
                }
            }
        }
        this.member = member;
        return components;
    }
}
