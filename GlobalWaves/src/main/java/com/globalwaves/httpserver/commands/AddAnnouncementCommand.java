package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;


public final class AddAnnouncementCommand extends Command implements CommandRunner {
    private String name;
    private String description;

    public AddAnnouncementCommand(final String command, final String username,
            final Integer timestamp, final String name, final String description) {
        super(command, username, timestamp);
        this.name = name;
        this.description = description;
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", database.addAnnouncement(getUsername(), name, description));
        return output;
    }
}
