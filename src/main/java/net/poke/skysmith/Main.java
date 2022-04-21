package net.poke.skysmith;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Builder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.util.StringUtils;
import com.uploadcare.api.Client;
import com.uploadcare.api.Project;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.poke.skysmith.command.CommandFramework;
import net.poke.skysmith.command.CommandListener;
import net.poke.skysmith.command.commands.CreateCMD;
import net.poke.skysmith.command.pages.revamped.events.PageListener;
import net.poke.skysmith.command.pages.revamped.pages.*;
import net.poke.skysmith.command.pages.revamped.Page;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Description;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Finish;
import net.poke.skysmith.command.pages.revamped.pages.createpage.Stats;
import net.poke.skysmith.command.pages.revamped.pages.description.*;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static JDA jda = null;
    public static Font regularFont = null;
    public static Font boldFont = null;
    public static Client client = null;
    public static Project project = null;
    public static AmazonS3 s3 = null;
    public static ArrayList<Page> PAGES = new ArrayList<Page>();
    public static Project.Collaborator owner = null;
    public static void main(String[] args) throws LoginException, IOException, URISyntaxException, FontFormatException {
        new Main().create();
    }
    public void create () throws LoginException, IOException, FontFormatException, URISyntaxException {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        regularFont = Font.createFont(Font.TRUETYPE_FONT, new File("MinecraftRegular.otf")).deriveFont(10f);
        ge.registerFont(regularFont);
        boldFont = Font.createFont(Font.TRUETYPE_FONT, new File("MinecraftBold.otf")).deriveFont(10f);
        ge.registerFont(boldFont);
        JDABuilder builder = JDABuilder.createDefault("OTU2NjU3Njg3MjEzNTEwNjY2.Yjza5g.LUCe1CW54Qrmp9-WgKuo_040pQk");
        builder.disableCache(Arrays.asList(CacheFlag.values()));
        builder.enableCache(CacheFlag.EMOTE);
        builder.setActivity(Activity.watching("people make items."));
        builder.addEventListeners(new CommandListener(), new PageListener());
        jda = builder.build();
        initCommands();
        registerPages();
        client = new Client("c20c51a4c8f8ffaa9ef7", "9ceb71406b00148119c9");
        AWSCredentials credentials = new BasicAWSCredentials("a91b4c4885614d05bd10fcb91c0c3853", "808b50860f95f65737050e2ec9dabdc0");

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        AmazonS3Builder<AmazonS3ClientBuilder, AmazonS3> builder2 = AmazonS3ClientBuilder.
                standard().
                withPathStyleAccessEnabled(true).
                withClientConfiguration(clientConfig).
                withCredentials(new AWSStaticCredentialsProvider(credentials)).
                withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("usc1.contabostorage.com", "us-central-1"));
        s3 = builder2.build();
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName() + "\t" +
                    StringUtils.fromDate(bucket.getCreationDate()));
        }
        project = client.getProject();
        owner = project.getOwner();
    }

    private void initCommands() {
        CommandFramework.add(new CreateCMD());
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
    }
}
