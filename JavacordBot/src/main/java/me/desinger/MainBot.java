package me.desinger;

import me.desinger.audio.AudioManager;
import me.desinger.audio.PlayerManager;
import me.desinger.audio.ServerMusicManager;
import me.desinger.utils.Commands;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.awt.*;

public class MainBot {
    public void startBot(String token) {
        FallbackLoggerConfiguration.setTrace(false);
        PlayerManager.init();
        new DiscordApiBuilder()
                .setToken(token)
                .setRecommendedTotalShards()
                .join()
                .loginAllShards()
                .forEach(shardFuture -> shardFuture
                        .thenAccept(MainBot::onShardLogin)
                        .exceptionally(ExceptionLogger.get())
                );
    }

    private static void onShardLogin(DiscordApi api){
        api.setAutomaticMessageCacheCleanupEnabled(true);
        api.setMessageCacheSize(10, 60 * 5);
        api.setReconnectDelay(attempt -> attempt * 2);

        api.addMessageCreateListener(event -> {
                    String[] args = event.getMessageContent().split("\\s+");
                    if (args[0].equals(",play") || args[0].equals(",p")) {
                        ServerMusicManager m = AudioManager.get(event.getServer().get().getId());
                        String query = event.getMessageContent().replace(args[0] + " ", "");
                        Commands.playCommand(query, m, event, event.getServer().get());
                    } else if(args[0].equals(",stop")) {
                        Commands.stopCommand(event, event.getServer().get());
                    } else if(args[0].equals(",skip") || args[0].equals(",s")) {
                        Commands.skipCommand(event, event.getServer().get());
                    } else if(args[0].equals(",queue") || args[0].equals(",q")) {
                        Commands.queueCommand(event, event.getServer().get());
                    } else if(args[0].equals(",leave") || args[0].equals(",l")) {
                        Commands.leaveCommand(event, event.getServer().get());
                    } else if(args[0].equals(",repeat") || args[0].equals(",r")) {
                        Commands.repeatCommand(event, event.getServer().get());
                    } else if(args[0].equals(",volume") || args[0].equals(",v")) {
                        if(args[1] != null)
                            Commands.volumeCommand(event, event.getServer().get(), Integer.parseInt(args[1]));
                        else
                            new MessageBuilder()
                                    .setEmbed(new EmbedBuilder()
                                            .setColor(Color.RED)
                                            .setTitle("Ошибочка!")
                                            .setDescription("Не хватает аргумента для команды: ,volume 0-100")).send(event.getChannel());
                    } else if(args[0].equals(",join") || args[0].equals(",j")) {
                        Commands.joinCommand(event);
                    }
                });
    }

}
