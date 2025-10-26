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
                .addEventListeners(new Bot(this.gm, this.playerGm))
                .build()
                .awaitReady();

        jda.retrieveCommands().queue(existingCommands -> {
            boolean pingExists = existingCommands.stream()
                    .anyMatch(c -> c.getName().equals("ping"));
            boolean warExists = existingCommands.stream()
                    .anyMatch(c -> c.getName().equals("war"));
            boolean trophiesExists = existingCommands.stream()
                    .anyMatch(c -> c.getName().equals("trophies"));
            boolean donationsExists = existingCommands.stream()
                    .anyMatch(c -> c.getName().equals("donations"));

            if (!pingExists) {
                jda.upsertCommand("ping", "Replies with Pong!").queue();
            }
            if (!warExists) {
                jda.upsertCommand("war", "Generates a war history chart")
                        .addOption(OptionType.STRING, "tag", "The player tag", true)
                        .queue();
            }
            if (!trophiesExists) {
                jda.upsertCommand("trophies", "Generates a trophy history chart")
                        .addOption(OptionType.STRING, "tag", "The player tag", true)
                        .queue();
            }
            if (!donationsExists) {
                jda.upsertCommand("donations", "Generates a donation history chart")
                        .addOption(OptionType.STRING, "tag", "The player tag", true)
                        .queue();
            }
        });
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