package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;

public final class GetTop5SongsCommand extends Command implements CommandRunner {
    public GetTop5SongsCommand(final String command,
            final String username, final Integer timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("result", database.getTop5Songs());
        return output;
    }
}
