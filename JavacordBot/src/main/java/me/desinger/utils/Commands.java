package me.desinger.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.desinger.audio.*;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.Iterator;

public class Commands {
    public static void playCommand(String query, ServerMusicManager m, MessageCreateEvent event, Server server){
        event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(voiceChannel -> {
            if(voiceChannel.canYouConnect() && voiceChannel.canYouSee() && voiceChannel.hasPermission(event.getApi().getYourself(), PermissionType.SPEAK)){
                if (!voiceChannel.isConnected(event.getApi().getYourself()) && server.getAudioConnection().isEmpty()) {
                    voiceChannel.connect().thenAccept(audioConnection -> {
                        AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                        audioConnection.setAudioSource(audio);
                        audioConnection.setSelfDeafened(true);
                        CommandUtil.playTrack(query, event.getChannel(), m, PlayerManager.getManager());
                    });
                } else if (server.getAudioConnection().isPresent()) {
                    server.getAudioConnection().ifPresent(audioConnection -> {
                        if(audioConnection.getChannel().getId() == voiceChannel.getId()) {
                            AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                            audioConnection.setAudioSource(audio);
                            audioConnection.setSelfDeafened(true);
                            CommandUtil.playTrack(query, event.getChannel(), m, PlayerManager.getManager());
                        } else {
                            new MessageBuilder()
                                    .setEmbed(new EmbedBuilder()
                                            .setColor(Color.RED)
                                            .setTitle("Ошибочка!")
                                            .setDescription("Ты не в голосовом канале вместе со мной!")).send(event.getChannel());
                        }
                    });
                }
            } else {
                new MessageBuilder()
                        .setEmbed(new EmbedBuilder()
                                .setColor(Color.RED)
                                .setTitle("Ошибочка!")
                                .setDescription("Я не могу подключиться к голосовому каналу или у меня нет прав на это!")).send(event.getChannel());
            }
        }, () -> new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Ошибочка!")
                        .setDescription("Ты сейчас не в голосовом канале!")).send(event.getChannel()));
    }
    public static void stopCommand(MessageCreateEvent event, Server server){
        server.getAudioConnection().ifPresentOrElse(connection -> {
            AudioManager.get(server.getId()).player.stopTrack();
            new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Успех!")
                            .setDescription("Я выключил эту песню!")).send(event.getChannel());

        }, () -> new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Ошибочка!")
                        .setDescription("Музыка сейчас не играет!")).send(event.getChannel()));
    }
    public static void skipCommand(MessageCreateEvent event, Server server){
        server.getAudioConnection().ifPresentOrElse(connection -> {
            AudioManager.get(server.getId()).scheduler.nextTrack();
            new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Я пропустил эту песню!")
                            .setDescription("Cледующая песня: " + AudioManager.get(server.getId()).player.getPlayingTrack().getInfo().title)).send(event.getChannel());

        }, () -> new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Ошибочка!")
                        .setDescription("Музыка сейчас не играет!")).send(event.getChannel()));
    }
    public static void queueCommand(MessageCreateEvent event, Server server) {
        Iterator<AudioTrack> list = AudioManager.get(server.getId()).scheduler.showQueue();
        String queue = "";
        while(list.hasNext())
            queue = queue + list.next().getInfo().title + "\n";
        new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setTitle("Сейчас играет: " + AudioManager.get(server.getId()).player.getPlayingTrack().getInfo().title + "\n" + "Очередь:")
                        .setDescription(queue)).send(event.getChannel());
    }
    public static void leaveCommand(MessageCreateEvent event, Server server) {
            server.getConnectedVoiceChannel(event.getApi().getYourself()).ifPresentOrElse(voiceChannel -> {
                server.getAudioConnection().ifPresentOrElse(connection -> {
                    AudioManager.get(server.getId()).player.stopTrack();
                    connection.close();
                    }, () -> new MessageBuilder()
                        .setEmbed(new EmbedBuilder()
                                .setColor(Color.RED)
                                .setTitle("Ошибочка!")
                                .setDescription("Я не в голосовом канале!")).send(event.getChannel()));
                }, () -> new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("Ошибочка!")
                            .setDescription("Я не в голосовом канале!")).send(event.getChannel()));
    }
    public static void repeatCommand(MessageCreateEvent event, Server server) {
        TrackScheduler scheduler = AudioManager.get(server.getId()).scheduler;
        scheduler.setRepeat();
        if(scheduler.getRepeat())
            new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Успех!")
                            .setDescription("Режим повторения успешно включён!")).send(event.getChannel());
        else
            new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Успех!")
                            .setDescription("Режим повторения успешно выключён!")).send(event.getChannel());
    }
    public static void volumeCommand(MessageCreateEvent event, Server server, int vol) {
        if(vol < 0 || vol > 100) {
            new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Ошибочка!")
                        .setDescription("Ты не можешь установить громкость на " + vol)).send(event.getChannel());
        } else {
            AudioManager.get(server.getId()).player.setVolume(vol);
            new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Успех!")
                            .setDescription("Ты установил громкость на " + vol)).send(event.getChannel());
        }
    }
    public static void joinCommand(MessageCreateEvent event) {
        event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(serverVoiceChannel -> {
            serverVoiceChannel.connect().thenAccept(audioConnection -> {
                audioConnection.setSelfDeafened(true);
            });
        }, () -> new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Ошибочка!")
                        .setDescription("Ты сейчас не в голосовом канале!")).send(event.getChannel()));
    }
}
