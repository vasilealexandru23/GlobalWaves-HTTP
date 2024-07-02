package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;


public final class RemoveAlbumCommand extends Command implements CommandRunner {
    private String albumName;

    public RemoveAlbumCommand(final String command, final String username,
            final Integer timestamp, final String albumName) {
        super(command, username, timestamp);
        this.albumName = albumName;
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", database.removeAlbum(getUsername(), albumName));
        return output;
    }
}
