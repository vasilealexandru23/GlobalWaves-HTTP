package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.audiocollection.AudioCollection;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;
import com.globalwaves.httpserver.users.UserNormal;

public final class ShuffleCommand extends Command implements CommandRunner {
    private Integer seed;

    public ShuffleCommand(final String command, final String username,
            final Integer timestamp, final Integer seed) {
        super(command, username, timestamp);
        this.seed = seed;
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", checkShuffle(database));
        return output;
    }

    private String checkShuffle(final Database database) {
        /* Get the guaranteed normal user. */
        UserNormal user = ((UserNormal) database.findMyUser(getUsername()));

        /* Check if user is online. */
        if (!user.isOnline()) {
            return getUsername() + " is offline.";
        }

        MusicPlayer player = user.getMusicPlayer();
        String noLOAD = "Please load a source before using the shuffle function.";
        String noPLAYLIST = "The loaded source is not a playlist or an album.";

        /* Check for load. */
        if (player.getPlayback().getCurrTrack() == null) {
            return noLOAD;
        }

        /* Check for playlist. */
        if (player.getPlayback().getCurrTrack().getType() == AudioCollection.AudioType.PODCAST
            || player.getPlayback().getCurrTrack().getType() == AudioCollection.AudioType.SONG) {
            return noPLAYLIST;
        }

        return player.getPlayback().changeShuffle(seed);
    }
}
