package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;
import com.globalwaves.httpserver.users.UserNormal;

public final class RepeatCommand extends Command implements CommandRunner {
    public RepeatCommand(final String command, final String username,
            final Integer timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", checkRepeat(database, getUsername()));
        return output;
    }

    private String checkRepeat(final Database database, final String username) {
        /* Get the guaranteed normal user. */
        UserNormal user = ((UserNormal) database.findMyUser(username));

        /* Check if user is online. */
        if (!user.isOnline()) {
            return username + " is offline.";
        }

        MusicPlayer player = user.getMusicPlayer();
        String noLOAD = "Please load a source before setting the repeat status.";

        /* Check for load. */
        if (player.getPlayback() == null || player.getPlayback().getCurrTrack() == null) {
            return noLOAD;
        }

        return player.getPlayback().repeat();
    }
}
