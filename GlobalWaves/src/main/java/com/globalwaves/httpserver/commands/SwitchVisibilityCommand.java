package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;


public final class SwitchVisibilityCommand extends Command implements CommandRunner {
    private Integer playlistID;

    public SwitchVisibilityCommand(final String command, final String username,
            final Integer timestamp, final Integer playlistID) {
        super(command, username, timestamp);
        this.playlistID = playlistID;
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();
        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", database.switchVisibility(getUsername(), playlistID));
        return output;
    }
}
