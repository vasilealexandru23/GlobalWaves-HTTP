package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;

public final class RemoveAnnouncementCommand extends Command implements CommandRunner {
    private String name;

    public RemoveAnnouncementCommand(final String command, final String username,
            final Integer timestamp, final String name) {
        super(command, username, timestamp);
        this.name = name;
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", database.removeAnnouncement(getUsername(), name));
        return output;
    }
}
