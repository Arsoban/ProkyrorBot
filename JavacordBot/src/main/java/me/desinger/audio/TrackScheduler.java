package me.desinger.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean repeat = false;
    private AudioTrack playingTrack;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public Iterator<AudioTrack> showQueue(){
        return queue.iterator();
    }

    public void nextTrack() {
        if(!repeat) {
            player.startTrack(queue.poll(), false);
            this.playingTrack = queue.poll();
        } else {
            player.playTrack(playingTrack.makeClone());
        }
    }

    public void setRepeat() {
        this.repeat = !repeat;
    }

    public boolean getRepeat() {
        return repeat;
    }

    public void setPlayingTrack(AudioTrack track) {
        this.playingTrack = track;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            nextTrack();
        }
    }
}
