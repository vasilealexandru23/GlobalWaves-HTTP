package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.audiocollection.AudioCollection;
import com.globalwaves.httpserver.audiocollection.Podcast;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;
import com.globalwaves.httpserver.users.UserNormal;

public final class BackwardCommand extends Command implements CommandRunner {
   public BackwardCommand(final String command, final String username,
            final Integer timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        /* Guaranteed that we will always have a normal user. */
        UserNormal user = ((UserNormal) database.findMyUser(getUsername()));

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        /* Check if user is online. */
        if (!user.isOnline()) {
            output.put("message", getUsername() + " is offline.");
        } else {
            output.put("message", backwardTrack(user.getMusicPlayer()));
        }

        return output;
    }

    private String backwardTrack(final MusicPlayer player) {
        String noLOAD = "Please load a source before attempting to forward.";
        String noPODCAST = "The loaded source is not a podcast.";

        player.getPlayback().checkPlayback();

        /* Check for load. */
        if (player.getPlayback().getCurrTrack() == null) {
            return noLOAD;
        }

        /* Check for podcast. */
        if (player.getPlayback().getCurrTrack().getType() != AudioCollection.AudioType.PODCAST) {
            return noPODCAST;
        }

        return ((Podcast) player.getPlayback().getCurrTrack()).backward(player.getPlayback());
    }
}
