package net.poke.skysmith.command.pages.revamped.interactions;

import net.dv8tion.jda.api.entities.Member;

public interface AfterOpen extends Interaction {
    void run(Member member);
}
