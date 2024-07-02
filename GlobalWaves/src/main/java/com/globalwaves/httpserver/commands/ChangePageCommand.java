package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;

public final class ChangePageCommand extends Command implements CommandRunner {
    private String nextPage;

    public ChangePageCommand(final String command, final String username, final Integer timestamp,
            final String nextPage) {
        super(command, username, timestamp);
        this.nextPage = nextPage;
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", database.changePage(getUsername(), nextPage));
        return output;
    }
}
