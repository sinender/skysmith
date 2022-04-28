package net.poke.skysmith.command.pages.revamped.events;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.interactions.Button;
import net.poke.skysmith.command.pages.revamped.interactions.Message;
import net.poke.skysmith.command.pages.revamped.interactions.Selection;
import org.jetbrains.annotations.NotNull;

import static net.poke.skysmith.Main.PAGES;

public class PageListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (Page page : PAGES) {
            if (page.getTitle().equals(event.getButton().getId().split("-")[0])) {
                if (page.get(Integer.parseInt(event.getButton().getId().split("-")[1])) instanceof Button){
                    ((Button) page.get(Integer.parseInt(event.getButton().getId().split("-")[1]))).run(event);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) { ;
        if (event.getMessage().getReferencedMessage() == null) return;
        if (event.getMessage().getReferencedMessage().getEmbeds().isEmpty()) return;
        for (Page page : PAGES) {
            if (page.getTitle().equals(event.getMessage().getReferencedMessage().getEmbeds().get(0).getTitle())) {
                if ((page.get(page.getItems().size() - 1)) != null && (page.get(page.getItems().size() - 1)) instanceof Message) {
                    ((Message)page.get(page.getItems().size() - 1)).run(event);
                }
            }
        }
    }

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        for (Page page : PAGES) {
            if (page.getTitle().equals(event.getSelectMenu().getId().split("-")[0])) {
                if (page.get(0) instanceof Selection) {
                    ((Selection)page.get(0)).run(event, event.getSelectedOptions().get(0));
                }
            }
        }
    }
}
