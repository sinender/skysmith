package net.poke.skysmith;

import com.uploadcare.api.Client;
import com.uploadcare.api.Project;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.poke.skysmith.command.CommandFramework;
import net.poke.skysmith.command.CommandListener;
import net.poke.skysmith.command.commands.CreateCMD;
import net.poke.skysmith.command.commands.EditItemCMD;
import net.poke.skysmith.command.commands.LoadItemCMD;
import net.poke.skysmith.command.pages.revamped.events.PageListener;
import net.poke.skysmith.command.pages.revamped.pages.*;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Description;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Finish;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Settings;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Stats;
import net.poke.skysmith.command.pages.revamped.pages.description.*;
import net.poke.skysmith.command.pages.revamped.pages.settings.Name;
import net.poke.skysmith.command.pages.revamped.pages.settings.Rarity;
import net.poke.skysmith.mongo.MongoManager;
import net.poke.skysmith.utils.Item;
import net.poke.skysmith.utils.Stat;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main extends ListenerAdapter {
    public static JDA jda = null;
    public static Font regularFont = null;
    public static Font boldFont = null;
    public static Font unicodeFont = null;
    public static Client client = null;
    public static Project project = null;
    public static ArrayList<Page> PAGES = new ArrayList<Page>();
    public static Project.Collaborator owner = null;
    public static MongoManager mongo;
    public static List<Item> cachedItems = new ArrayList<>();
    public static void main(String[] args) throws LoginException, IOException, URISyntaxException, FontFormatException {
        new Main().create();
    }
    public void create () throws LoginException, IOException, FontFormatException, URISyntaxException {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        regularFont = Font.createFont(Font.TRUETYPE_FONT, new File("MinecraftRegular.otf")).deriveFont(22f);
        ge.registerFont(regularFont);
        unicodeFont = Font.createFont(Font.TRUETYPE_FONT, new File("MinecraftNormal.ttf")).deriveFont(16f);
        ge.registerFont(unicodeFont);
        boldFont = Font.createFont(Font.TRUETYPE_FONT, new File("MinecraftBold.otf")).deriveFont(22f);
        ge.registerFont(boldFont);
        JDABuilder builder = JDABuilder.createDefault("OTU2NjU3Njg3MjEzNTEwNjY2.Yjza5g.LUCe1CW54Qrmp9-WgKuo_040pQk");
        builder.disableCache(Arrays.asList(CacheFlag.values()));
        builder.enableCache(CacheFlag.EMOTE);
        builder.setActivity(Activity.watching("people make items."));
        builder.addEventListeners(new CommandListener(), new PageListener(), this);
        jda = builder.build();
        registerPages();
        client = new Client("c20c51a4c8f8ffaa9ef7", "9ceb71406b00148119c9");
        project = client.getProject();
        owner = project.getOwner();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        mongo = new MongoManager();
        mongo.connect();
        initCommands();
        mongo.getAllDocuments().forEach(doc -> {
            Item item = new Item(doc.getString("_id"), doc.getString("name"), doc.getList("description", String.class), doc.getString("rarity"));
            item.pureLore = doc.getBoolean("pureLore");
            item.numberedLore = doc.getBoolean("numberedLore");
            Document stats = doc.get("stats", Document.class);
            for (Stat stat : Stat.values()) {
                if (stats.containsKey(stat.name())) {
                    item.stats.put(stat, stats.getInteger(stat.name()));
                }
            }
            cachedItems.add(item);
            System.out.println("Loaded item: " + item.name + "\nWith lore: " + item.getLore());
            String memberId = doc.getString("owner");
            ArrayList<Item> items = Item.memberItems.get(memberId) == null ? new ArrayList<>() : Item.memberItems.get(memberId);
            items.add(item);
            Item.memberItems.put(memberId, items);
        });
        System.out.println("Ready!");
    }

    private void initCommands() {
        CommandFramework.add(new CreateCMD());
        CommandFramework.add(new EditItemCMD(null));
        CommandFramework.add(new LoadItemCMD());
    }

    private void registerPages() {
        PAGES.add(new Description());
        PAGES.add(new CreatePage());
        PAGES.add(new AddLines());
        PAGES.add(new RemoveLine(null));
        PAGES.add(new SetLines());
        PAGES.add(new OneLine(null, null));
        PAGES.add(new MultipleLines());
        PAGES.add(new Finish(null));
        PAGES.add(new Stats(null));
        PAGES.add(new Settings());
        PAGES.add(new Rarity());
        PAGES.add(new Name());
        PAGES.add(new SelectItem(null));
    }
}
