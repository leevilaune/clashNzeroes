package com.onesnzeroes.clashnzeroes.logic.discord;

import com.onesnzeroes.clashnzeroes.logic.graphic.GraphicManager;
import com.onesnzeroes.clashnzeroes.logic.graphic.PlayerGraphicManager;
import com.onesnzeroes.clashnzeroes.logic.scheduler.ImageRemovalScheduler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

public class Bot extends ListenerAdapter {

    private GraphicManager gm;
    private PlayerGraphicManager playerGm;
    private ImageRemovalScheduler scheduler;

    public Bot(GraphicManager gm, PlayerGraphicManager playerGm){
        this.gm = gm;
        this.playerGm = playerGm;
        this.scheduler = new ImageRemovalScheduler(60000);
        scheduler.schedule();
    }

    public void start() throws InterruptedException {
        String token = System.getenv("DISCORD_TOKEN");

        var jda = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("Overgrowth + zapquake!"))
                .addEventListeners(new Bot(this.gm,this.playerGm))
                .build()
                .awaitReady();

        jda.updateCommands().addCommands(
                Commands.slash("ping", "Replies with Pong!"),
                Commands.slash("war", "Generates a war history chart")
                        .addOption(OptionType.STRING, "tag", "The player tag", true),
                Commands.slash("trophies", "Generates a trophy history chart")
                        .addOption(OptionType.STRING,
                                "tag", "The player tag", true),
                Commands.slash("donations", "Generates a donation history chart")
                        .addOption(OptionType.STRING,
                                "tag", "The player tag", true)
        ).queue();

        var guild = jda.getGuildById("807397412586258432");
        if (guild != null) {
            guild.updateCommands().addCommands(
                    Commands.slash("ping", "Replies with Pong!"),
                    Commands.slash("war", "Generates a war history chart")
                            .addOption(OptionType.STRING,
                                    "tag", "The player tag", true),
                    Commands.slash("trophies", "Generates a trophy history chart")
                            .addOption(OptionType.STRING,
                                    "tag", "The player tag", true),
                    Commands.slash("donations", "Generates a donation history chart")
                            .addOption(OptionType.STRING,
                                    "tag", "The player tag", true)
            ).queue();
            System.out.println("Guild commands registered for " + guild.getName());
        } else {
            System.err.println("Guild not found! Check the guild ID.");
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.reply("Pong! 🏓").queue();
        }
        if (event.getName().equals("war")) {
            event.deferReply().queue();

            String playerTag = event.getOption("tag").getAsString();

            this.gm.setTag(playerTag);
            this.gm.setTitle("war");
            this.gm.generateChartAsync()
                    .thenAccept(file -> {
                        event.getHook()
                                .sendMessage("**" + playerTag + "**")
                                .addFiles(FileUpload.fromData(file))
                                .queue();
                    this.scheduler.addImage(file.getAbsolutePath());
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        event.getHook().sendMessage("Error generating chart.").queue();
                        return null;
                    });
        }
        if (event.getName().equals("trophies")) {
            event.deferReply().queue();

            String playerTag = event.getOption("tag").getAsString();

            this.playerGm.setTag(playerTag);
            this.playerGm.setField("trophies");
            this.playerGm.generateChartAsync()
                    .thenAccept(file -> {
                        event.getHook()
                                .sendMessage("**" + playerTag + "**")
                                .addFiles(FileUpload.fromData(file))
                                .queue();
                        this.scheduler.addImage(file.getAbsolutePath());
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        event.getHook().sendMessage("Error generating chart.").queue();
                        return null;
                    });
        }
        else if (event.getName().equals("donations")) {
            event.deferReply().queue();

            String playerTag = event.getOption("tag").getAsString();

            this.playerGm.setTag(playerTag);
            this.playerGm.setTitle("donations");
            this.playerGm.generateChartAsync()
                    .thenAccept(file -> {
                        event.getHook()
                                .sendMessage("**" + playerTag + "**")
                                .addFiles(FileUpload.fromData(file))
                                .queue();
                        this.scheduler.addImage(file.getAbsolutePath());
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        event.getHook().sendMessage("Error generating chart.").queue();
                        return null;
                    });
        }
    }
}