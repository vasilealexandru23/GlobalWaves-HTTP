package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;
import com.globalwaves.httpserver.users.UserNormal;


public final class StatusCommand extends Command implements CommandRunner {
    public StatusCommand(final String command, final String username,
            final Integer timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        /* Guaranteed that we will always have a normal user. */
        UserNormal user = ((UserNormal) database.findMyUser(getUsername()));
        if (user == null) {
            output.put("message", "User not found.");
            return output;
        }

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

         /* Check if user is online. */

        output.put("stats", getStatus(user));

        return output;
    }

    /**
     * Function that returns the status of the user.
     * @param user          the user that request the status
     * @return              stats of user
     */
    private ObjectNode getStatus(final UserNormal user) {
        ObjectNode statusOutput = new ObjectMapper().createObjectNode();

        MusicPlayer player = user.getMusicPlayer();
        player.checkStatus();

        statusOutput.put("name", player.getPlayback().getTrack());
        statusOutput.put("remainedTime", player.getPlayback().getTimeRemained());
        statusOutput.put("repeat", player.getPlayback().getRepeatstatus());
        statusOutput.put("shuffle", player.getPlayback().isShuffle());
        statusOutput.put("paused", !player.getPlayback().isPlayPause());

        return statusOutput;
    }
}
